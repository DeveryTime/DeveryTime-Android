package com.example.deverytime_android2

import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UpdatePostTest {
    // 서버 접속 없는 수정 API
    private fun api(status: Int, json: String, checkRequest: (okhttp3.Request) -> Unit): PostApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            checkRequest(chain.request())
            okhttp3.Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(status)
                .message("Test response")
                .body(json.toResponseBody("application/json".toMediaType()))
                .build()
        }.build()
        return Retrofit.Builder().baseUrl("https://example.test/")
            .client(client).addConverterFactory(GsonConverterFactory.create())
            .build().create(PostApi::class.java)
    }

    // PUT 경로·요청 필드·수정 응답 검증
    @Test
    fun updateRequestContainsOnlyUserIdTitleAndContent() = runBlocking {
        val api = api(200, """{"id":15,"title":"수정 제목","updatedAt":"2026-08-04T13:00:00"}""") { request ->
            assertEquals("PUT", request.method)
            assertEquals("/api/posts/15", request.url.encodedPath)
            val buffer = okio.Buffer()
            request.body!!.writeTo(buffer)
            val body = JsonParser.parseString(buffer.readUtf8()).asJsonObject
            assertEquals(3, body.size())
            assertEquals(1L, body["userId"].asLong)
            assertEquals("수정 제목", body["title"].asString)
            assertEquals("수정 내용", body["content"].asString)
        }
        val response = PostRepository(api).updatePost(15L, UpdatePostRequest(1L, "수정 제목", "수정 내용"))
        assertEquals(200, response.code())
        assertEquals(UpdatePostResponse(15L, "수정 제목", "2026-08-04T13:00:00"), response.body())
    }

    // 빈 403 응답 처리 검증
    @Test
    fun emptyForbiddenResponseShowsPermissionMessage() = runBlocking {
        val api = api(403, "") { }
        val response = PostRepository(api).updatePost(15L, UpdatePostRequest(1L, "제목", "내용"))
        assertFalse(response.isSuccessful)
        assertEquals("해당 작업을 수행할 권한이 없습니다.", updatePostErrorMessage(response.code(), response.errorBody()?.string()))
    }

    // 검증 오류의 서버 문구 우선 적용
    @Test
    fun validationErrorUsesServerMessageWithFieldDetails() {
        val json = """{"success":false,"error":{"code":"VALIDATION_ERROR","message":"입력값이 올바르지 않습니다.","details":[{"field":"title","message":"필수 입력 항목입니다."}]}}"""
        assertEquals("입력값이 올바르지 않습니다.", updatePostErrorMessage(400, json))
    }

    // 오류 코드·HTTP 기본 문구 검증
    @Test
    fun missingAndMalformedErrorsHaveFallbackMessages() {
        assertEquals("존재하지 않는 게시글입니다.", updatePostErrorMessage(400, """{"error":{"code":"POST_NOT_FOUND"}}"""))
        assertEquals("존재하지 않는 게시글입니다.", updatePostErrorMessage(404, null))
        assertEquals("로그인이 필요합니다. 다시 로그인해 주세요.", updatePostErrorMessage(401, ""))
        assertEquals("게시글 수정에 실패했습니다. (HTTP 500)", updatePostErrorMessage(500, "<html>error</html>"))
    }

    // 사용자 ID 누락 시 수정 요청 차단
    @Test
    fun missingCurrentUserIdStopsUpdateBeforeSending() {
        val model = PostingViewModel()
        model.updatePost(15L, null, "제목", "내용")
        assertEquals(PostingUiState.Error("로그인한 사용자 정보를 확인할 수 없습니다."), model.uiState.value)
    }
}
