package com.example.deverytime_android2

import com.google.gson.JsonParser
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

class CreateCommentTest {
    // 서버 접속 없는 댓글 API·요청 본문 검증
    private fun api(status: Int, json: String): CommentApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            assertEquals("POST", request.method)
            assertEquals("/api/posts/10/comments", request.url.encodedPath)
            assertNull(request.url.query)
            val buffer = okio.Buffer()
            request.body!!.writeTo(buffer)
            val body = JsonParser.parseString(buffer.readUtf8()).asJsonObject
            assertEquals(1, body.size())
            assertEquals("좋은 글이네요!", body["content"].asString)
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

    // 201·성공 응답·등록 댓글 검증
    @Test
    fun createsCommentUsingOnlyContentAndParsesEnvelope() = runBlocking {
        val json = """{"success":true,"data":{"id":1,"postId":10,"userId":5,"content":"좋은 글이네요!","createdAt":"2026-08-04T22:00:00"},"message":"댓글이 등록되었습니다."}"""
        val response = CommentRepository(api(201, json)).createComment(10L, "좋은 글이네요!")
        val comment = CommentData(1L, 10L, 5L, "좋은 글이네요!", "2026-08-04T22:00:00")
        assertEquals(CreateCommentUiState.Success(comment, "댓글이 등록되었습니다."), createCommentResult(response))
    }

    // 잘못된 성공 응답 차단
    @Test
    fun rejectsMissingDataFalseSuccessAndUnexpectedStatus() {
        val error = CreateCommentUiState.Error("댓글 작성 응답을 확인할 수 없습니다.")
        val comment = CommentData(1L, 10L, 5L, "내용", "2026-08-04T22:00:00")
        assertEquals(error, createCommentResult(Response.success(201, CreateCommentResponse(false, comment, null))))
        assertEquals(error, createCommentResult(Response.success(201, CreateCommentResponse(true, null, null))))
        assertEquals(error, createCommentResult(Response.success<CreateCommentResponse>(201, null)))
        assertEquals(error, createCommentResult(Response.success(200, CreateCommentResponse(true, comment, null))))
        assertEquals(CreateCommentUiState.Success(comment, "댓글이 등록되었습니다."),
            createCommentResult(Response.success(201, CreateCommentResponse(true, comment, ""))))
    }

    // 빈 401·500 응답 처리 검증
    @Test
    fun emptyUnauthorizedAndServerErrorsShowFallbacks() = runBlocking {
        val unauthorized = CommentRepository(api(401, "")).createComment(10L, "좋은 글이네요!")
        assertEquals(CreateCommentUiState.Error("로그인이 필요합니다. 다시 로그인해 주세요."), createCommentResult(unauthorized))
        val serverError = CommentRepository(api(500, "")).createComment(10L, "좋은 글이네요!")
        assertEquals(CreateCommentUiState.Error("서버 측 오류가 발생했습니다."), createCommentResult(serverError))
    }

    // 서버 검증 문구·오류 코드·잘못된 JSON 검증
    @Test
    fun validationAndMalformedErrorsHaveReadableMessages() {
        val json = """{"success":false,"error":{"code":"VALIDATION_ERROR","message":"필수 입력 항목입니다.","details":[{"field":"content","message":"필수 입력 항목입니다."}]}}"""
        assertEquals("필수 입력 항목입니다.", createCommentErrorMessage(400, json))
        assertEquals("댓글 내용을 확인해 주세요.", createCommentErrorMessage(400, null))
        assertEquals("서버 측 오류가 발생했습니다.", createCommentErrorMessage(500, "<html>error</html>"))
        assertEquals("서버 측 오류가 발생했습니다.", createCommentErrorMessage(400, """{"error":{"code":"INTERNAL_SERVER_ERR"}}"""))
        assertEquals("댓글 작성에 실패했습니다. (HTTP 503)", createCommentErrorMessage(503, null))
    }

    // 공백 댓글 전송 차단
    @Test
    fun blankCommentStopsBeforeNetworkRequest() {
        val model = CommentViewModel()
        model.createComment(10L, "  \n\t  ")
        assertEquals(CreateCommentUiState.Error("댓글 내용을 입력해 주세요."), model.state.value)
    }
}
