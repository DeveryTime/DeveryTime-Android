package com.example.deverytime_android2

import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeletePostTest {
    // 서버 접속 없는 삭제 API
    private fun api(status: Int, json: String): PostApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            assertEquals("DELETE", request.method)
            assertEquals("/api/posts/15", request.url.encodedPath)
            assertNull(request.body)
            assertNull(request.url.query)
            okhttp3.Response.Builder()
                .request(request)
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

    // 요청 본문 없는 삭제·204 빈 응답 검증
    @Test
    fun deletionAccepts204WithoutResponseBody() = runBlocking {
        val response = PostRepository(api(204, "")).deletePost(15L)
        assertEquals(204, response.code())
        assertNull(response.body())
    }

    // 빈 403 응답 처리 검증
    @Test
    fun emptyForbiddenResponseShowsPermissionMessage() = runBlocking {
        val response = PostRepository(api(403, "")).deletePost(15L)
        assertFalse(response.isSuccessful)
        assertEquals("해당 작업을 수행할 권한이 없습니다.", deletePostErrorMessage(response.code(), response.errorBody()?.string()))
    }

    // 없는 게시글의 서버 오류 검증
    @Test
    fun notFoundResponseUsesServerMessage() = runBlocking {
        val json = """{"success":false,"error":{"code":"POST_NOT_FOUND","message":"존재하지 않는 게시글입니다."}}"""
        val response = PostRepository(api(404, json)).deletePost(15L)
        assertEquals("존재하지 않는 게시글입니다.", deletePostErrorMessage(response.code(), response.errorBody()?.string()))
    }

    // 오류 코드·응답 누락 시 기본 문구 검증
    @Test
    fun missingAndMalformedErrorsHaveFallbackMessages() {
        assertEquals("해당 작업을 수행할 권한이 없습니다.", deletePostErrorMessage(400, """{"error":{"code":"FORBIDDEN"}}"""))
        assertEquals("존재하지 않는 게시글입니다.", deletePostErrorMessage(400, """{"error":{"code":"POST_NOT_FOUND"}}"""))
        assertEquals("로그인이 필요합니다. 다시 로그인해 주세요.", deletePostErrorMessage(401, null))
        assertEquals("게시글 삭제에 실패했습니다. (HTTP 500)", deletePostErrorMessage(500, "<html>error</html>"))
    }
}
