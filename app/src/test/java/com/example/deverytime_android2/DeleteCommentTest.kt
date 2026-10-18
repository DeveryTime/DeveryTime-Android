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

class DeleteCommentTest {
    // PATCH 경로·요청 값 없음 검증
    private fun api(status: Int, json: String): CommentApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            assertEquals("PATCH", request.method)
            assertEquals("/api/posts/4294967296/comments/4294967297", request.url.encodedPath)
            assertNull(request.url.query)
            assertEquals(0L, request.body?.contentLength() ?: 0L)
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

    // data:null 삭제 성공 검증
    @Test
    fun acceptsSuccessWithNullData() = runBlocking {
        val json = """{"success":true,"data":null,"message":"댓글이 삭제되었습니다."}"""
        val response = CommentRepository(api(200, json)).deleteComment(4294967296L, 4294967297L)
        assertNull(response.body()!!.data)
        assertEquals(DeleteCommentUiState.Success("댓글이 삭제되었습니다."), deleteCommentResult(response))
    }

    // 실패 응답·본문 누락·성공 메시지 누락 검증
    @Test
    fun validatesSuccessEnvelopeAndUsesDefaultMessage() {
        val error = DeleteCommentUiState.Error("댓글 삭제 응답을 확인할 수 없습니다.")
        assertEquals(error, deleteCommentResult(Response.success(200, DeleteCommentResponse(false, null, null))))
        assertEquals(error, deleteCommentResult(Response.success<DeleteCommentResponse>(200, null)))
        assertEquals(error, deleteCommentResult(Response.success(201, DeleteCommentResponse(true, null, null))))
        assertEquals(DeleteCommentUiState.Success("댓글이 삭제되었습니다."),
            deleteCommentResult(Response.success(200, DeleteCommentResponse(true, null, ""))))
    }

    // 빈 인증 오류·서버 오류 검증
    @Test
    fun handlesEmptyUnauthorizedAndServerErrors() = runBlocking {
        val unauthorized = CommentRepository(api(401, "")).deleteComment(4294967296L, 4294967297L)
        assertEquals(DeleteCommentUiState.Error("로그인이 필요합니다. 다시 로그인해 주세요."), deleteCommentResult(unauthorized))
        val serverError = CommentRepository(api(500, "")).deleteComment(4294967296L, 4294967297L)
        assertEquals(DeleteCommentUiState.Error("서버 측 오류가 발생했습니다."), deleteCommentResult(serverError))
    }

    // 명세의 오류 코드·서버 메시지 우선 검증
    @Test
    fun handlesDocumentedAccessDeniedAndNotFound() = runBlocking {
        val deniedJson = """{"success":false,"error":{"code":"POST_ACCESS_DENIED","message":"본인 게시글만 수정/삭제할 수 있습니다."}}"""
        val denied = CommentRepository(api(403, deniedJson)).deleteComment(4294967296L, 4294967297L)
        assertEquals(DeleteCommentUiState.Error("본인 게시글만 수정/삭제할 수 있습니다."), deleteCommentResult(denied))
        val missingJson = """{"success":false,"error":{"code":"CATEGORY_NOT_FOUND","message":"존재하지 않는 카테고리입니다."}}"""
        val missing = CommentRepository(api(404, missingJson)).deleteComment(4294967296L, 4294967297L)
        assertEquals(DeleteCommentUiState.Error("존재하지 않는 카테고리입니다."), deleteCommentResult(missing))
    }

    // 댓글용 코드·메시지 누락·잘못된 JSON 검증
    @Test
    fun handlesCommentCodesAndMalformedErrors() {
        for (code in listOf("COMMENT_ACCESS_DENIED", "POST_ACCESS_DENIED")) {
            assertEquals("본인 댓글만 삭제할 수 있습니다.", deleteCommentErrorMessage(400, """{"error":{"code":"$code"}}"""))
        }
        for (code in listOf("COMMENT_NOT_FOUND", "CATEGORY_NOT_FOUND")) {
            assertEquals("존재하지 않는 댓글입니다.", deleteCommentErrorMessage(400, """{"error":{"code":"$code"}}"""))
        }
        assertEquals("존재하지 않는 게시글입니다.", deleteCommentErrorMessage(400, """{"error":{"code":"POST_NOT_FOUND"}}"""))
        assertEquals("로그인이 필요합니다. 다시 로그인해 주세요.", deleteCommentErrorMessage(400, """{"error":{"code":"UNAUTHORIZED"}}"""))
        assertEquals("서버 측 오류가 발생했습니다.", deleteCommentErrorMessage(400, """{"error":{"code":"INTERNAL_SERVER_ERR"}}"""))
        assertEquals("서버 측 오류가 발생했습니다.", deleteCommentErrorMessage(500, "<html>error</html>"))
        assertEquals("댓글 삭제에 실패했습니다. (HTTP 503)", deleteCommentErrorMessage(503, null))
    }
}
