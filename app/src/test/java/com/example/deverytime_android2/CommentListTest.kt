package com.example.deverytime_android2

import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentListTest {
    // 서버 접속 없는 목록 요청 검증
    private fun api(status: Int, json: String): CommentApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            assertEquals("GET", request.method)
            assertEquals("/api/posts/4294967296/comments", request.url.encodedPath)
            assertNull(request.url.query)
            assertNull(request.body)
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
            .build().create(CommentApi::class.java)
    }

    // 작성자·프로필·댓글 순서 검증
    @Test
    fun readsCommentsAndNullableProfiles() = runBlocking {
        val json = """{"success":true,"data":[
            {"id":1,"userId":5,"authorName":"홍길동","profileImageUrl":"https://example.test/5.jpg","content":"첫 번째 댓글입니다.","createdAt":"2026-08-04T22:00:00"},
            {"id":2,"userId":8,"authorName":"이순신","profileImageUrl":null,"content":"두 번째 댓글입니다.","createdAt":"2026-08-04T22:05:00"}
        ]}"""
        val response = CommentRepository(api(200, json)).getComments(4294967296L)
        val comments = listOf(
            CommentListItem(1L, 5L, "홍길동", "https://example.test/5.jpg", "첫 번째 댓글입니다.", "2026-08-04T22:00:00"),
            CommentListItem(2L, 8L, "이순신", null, "두 번째 댓글입니다.", "2026-08-04T22:05:00"),
        )
        assertEquals(CommentListUiState.Success(comments), commentListResult(response))
    }

    // 빈 배열 정상 처리 검증
    @Test
    fun acceptsEmptyList() = runBlocking {
        val response = CommentRepository(api(200, """{"success":true,"data":[]}""")).getComments(4294967296L)
        assertEquals(CommentListUiState.Success(emptyList()), commentListResult(response))
    }

    // 목록 누락·실패 응답 차단
    @Test
    fun rejectsMissingDataFalseSuccessAndUnexpectedStatus() {
        val error = CommentListUiState.Error("댓글 목록 응답을 확인할 수 없습니다.")
        assertEquals(error, commentListResult(Response.success(200, CommentListResponse(true, null))))
        assertEquals(error, commentListResult(Response.success(200, CommentListResponse(false, emptyList()))))
        assertEquals(error, commentListResult(Response.success<CommentListResponse>(200, null)))
        assertEquals(error, commentListResult(Response.success(201, CommentListResponse(true, emptyList()))))
    }

    // 서버 오류·없는 게시글 응답 검증
    @Test
    fun handlesEmptyServerErrorAndNotFound() = runBlocking {
        val serverError = CommentRepository(api(500, "")).getComments(4294967296L)
        assertEquals(CommentListUiState.Error("서버 측 오류가 발생했습니다."), commentListResult(serverError))
        val json = """{"success":false,"error":{"code":"POST_NOT_FOUND","message":"존재하지 않는 게시글입니다."}}"""
        val notFound = CommentRepository(api(404, json)).getComments(4294967296L)
        assertEquals(CommentListUiState.Error("존재하지 않는 게시글입니다."), commentListResult(notFound))
    }

    // 오류 코드·서버 메시지·잘못된 JSON 검증
    @Test
    fun choosesServerMessageOrReadableFallback() {
        assertEquals("서버 점검 중입니다.", commentListErrorMessage(500, """{"error":{"code":"INTERNAL_SERVER_ERR","message":"서버 점검 중입니다."}}"""))
        assertEquals("존재하지 않는 게시글입니다.", commentListErrorMessage(400, """{"error":{"code":"POST_NOT_FOUND"}}"""))
        assertEquals("서버 측 오류가 발생했습니다.", commentListErrorMessage(400, """{"error":{"code":"INTERNAL_SERVER_ERR"}}"""))
        assertEquals("서버 측 오류가 발생했습니다.", commentListErrorMessage(500, "<html>error</html>"))
        assertEquals("댓글 목록을 불러오지 못했습니다. (HTTP 503)", commentListErrorMessage(503, null))
    }
}
