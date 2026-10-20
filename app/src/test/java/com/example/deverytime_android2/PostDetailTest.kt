package com.example.deverytime_android2

import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostDetailTest {
    // 서버 접속 없는 상세 조회 API
    private fun api(id: Long, status: Int, json: String): PostApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            assertEquals("GET", request.method)
            assertEquals("/api/posts/$id", request.url.encodedPath)
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

    // 큰 게시글 ID·상세 응답 해석 검증
    @Test
    fun detailRequestParsesWriterCategoryImagesAndNullUpdatedAt() = runBlocking {
        val id = 4294967296L
        val json = """{"success":true,"data":{
            "id":4294967296,"title":"제목","content":"내용","status":"PUBLISHED","viewCount":43,
            "writerNickname":"닉네임","writerProfileImageUrl":"https://example.test/profile.jpg",
            "categoryName":"프로젝트",
            "imageUrls":["https://example.test/img1.png","https://example.test/img2.png"],
            "createdAt":"2026-08-04T12:30:00","updatedAt":null,"likeCount":1,"liked":true
        }}"""
        val response = PostRepository(api(id, 200, json)).getPost(id)
        val post = response.body()!!
        assertEquals(200, response.code())
        assertEquals(id, post.id)
        assertEquals("내용", post.content)
        assertEquals(43, post.viewCount)
        assertEquals("제목", post.title)
        assertEquals("2026-08-04T12:30:00", post.createdAt)
        assertEquals(PostWriter(null, "닉네임", "https://example.test/profile.jpg"), post.writer)
        assertEquals(PostDetailCategory(null, "프로젝트"), post.category)
        assertEquals(
            listOf(
                PostImage(null, "https://example.test/img1.png", 0),
                PostImage(null, "https://example.test/img2.png", 1),
            ),
            post.images,
        )
        assertEquals(1, post.likeCount)
        assertEquals(true, post.liked)
        assertNull(post.updatedAt)
    }

    // 이미지 없는 게시글·프로필 누락 검증
    @Test
    fun detailResponseAllowsEmptyImagesAndNullProfile() = runBlocking {
        val json = """{"success":true,"data":{
            "id":1,"title":"댓글 테스트 게시글","content":"Postman 테스트용 게시글입니다.",
            "status":"PUBLISHED","viewCount":1,"writerNickname":"테스트",
            "writerProfileImageUrl":null,"categoryName":"테스트","imageUrls":[],
            "createdAt":"2026-09-08T19:19:41","updatedAt":"2026-09-18T19:58:49.369051",
            "likeCount":1,"liked":true
        }}"""
        val post = PostRepository(api(1L, 200, json)).getPost(1L).body()!!
        assertTrue(post.images.isEmpty())
        assertNull(post.writer.userId)
        assertNull(post.writer.profileImageUrl)
        assertEquals("테스트", post.writer.nickname)
        assertEquals("테스트", post.category.name)
        assertEquals("2026-09-08T19:19:41", post.createdAt)
        assertEquals("2026-09-18T19:58:49.369051", post.updatedAt)
    }

    // data 누락·실패 응답은 정상 상세 정보로 전달하지 않음
    @Test
    fun missingDataAndUnsuccessfulResponseDoNotProduceDetail() = runBlocking {
        for (json in listOf("""{"success":true,"data":null}""", """{"success":false}""")) {
            assertNull(PostRepository(api(1L, 200, json)).getPost(1L).body())
        }
    }

    // 작성일 누락으로 상세 화면의 날짜 변환 함수가 크래시하지 않도록 사전 검증
    @Test
    fun missingCreatedAtIsRejectedBeforeRendering() {
        val json = """{"success":true,"data":{
            "id":1,"title":"제목","content":"내용","status":"PUBLISHED","viewCount":0,
            "writerNickname":"테스트","categoryName":"테스트","imageUrls":[]
        }}"""
        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking { PostRepository(api(1L, 200, json)).getPost(1L) }
        }
        assertEquals("상세 응답의 createdAt이 없습니다.", exception.message)
    }

    // 존재하지 않는 게시글의 서버 오류 검증
    @Test
    fun notFoundResponseUsesServerMessage() = runBlocking {
        val json = """{"success":false,"error":{"code":"POST_NOT_FOUND","message":"존재하지 않는 게시글입니다."}}"""
        val response = PostRepository(api(15L, 404, json)).getPost(15L)
        assertEquals("존재하지 않는 게시글입니다.", postDetailErrorMessage(response.code(), response.errorBody()?.string()))
    }

    // 오류 문구 누락·형식 오류 시 기본 문구 검증
    @Test
    fun missingAndMalformedErrorsHaveFallbackMessages() {
        assertEquals("존재하지 않는 게시글입니다.", postDetailErrorMessage(400, """{"error":{"code":"POST_NOT_FOUND"}}"""))
        assertEquals("존재하지 않는 게시글입니다.", postDetailErrorMessage(404, null))
        assertEquals("로그인이 필요합니다. 다시 로그인해 주세요.", postDetailErrorMessage(401, ""))
        assertEquals("게시글을 불러오지 못했습니다. (HTTP 500)", postDetailErrorMessage(500, "<html>error</html>"))
    }
}
