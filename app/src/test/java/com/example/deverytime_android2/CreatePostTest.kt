package com.example.deverytime_android2

import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import retrofit2.Response

class CreatePostTest {
    // 공개 상태 전송 검증
    @Test
    fun requestAlwaysSerializesPublishedStatus() {
        val json = JsonParser.parseString(
            Gson().toJson(CreatePostRequest(3, "제목", "내용")),
        ).asJsonObject

        assertEquals(4, json.size())
        assertEquals(3, json["categoryId"].asInt)
        assertEquals("제목", json["title"].asString)
        assertEquals("내용", json["content"].asString)
        assertEquals("PUBLISHED", json["status"].asString)
    }

    // 요청·응답 전달 검증
    @Test
    fun repositoryPassesRequestAndCreatedResponseThrough() = runBlocking {
        val request = CreatePostRequest(3, "제목", "내용")
        val expectedRequest = request
        val created = CreatePostResponse(15, "제목", "PUBLISHED", "2026-08-04T12:30:00")
        val api = object : PostApi {
            override suspend fun deletePost(id: Long): Response<Unit> =
                error("Post deletion is not part of this test")

            override suspend fun updatePost(id: Long, request: UpdatePostRequest): Response<UpdatePostResponse> =
                error("Post update is not part of this test")

            override suspend fun getPost(id: Long): Response<PostDetailResponse> =
                error("Post detail is not part of this test")

            override suspend fun getPosts(page: Int, size: Int, sort: String, categoryId: Long?): Response<PostListResponse> =
                error("Post list is not part of this test")

            override suspend fun createPost(request: CreatePostRequest): Response<CreatePostResponse> {
                assertSame(expectedRequest, request)
                return Response.success(201, created)
            }

            override suspend fun searchPosts(request: SearchRequest): SearchResponse =
                error("Search is not part of this test")

            override suspend fun getCategories(): Response<List<PostCategory>> =
                Response.success(listOf(PostCategory(3, "교과")))
        }

        val response = PostRepository(api).createPost(request)
        assertEquals(201, response.code())
        assertSame(created, response.body())
        assertEquals(listOf(PostCategory(3, "교과")), PostRepository(api).getCategories().body())
    }

    // 서버 입력 오류 문구 검증
    @Test
    fun validationResponseWithoutDetailsUsesServerMessage() {
        assertEquals(
            "필수 입력 항목입니다.",
            postErrorMessage(400, """{"success":false,"status":400,"error":{"code":"VALIDATION_ERROR","message":"필수 입력 항목입니다."}}"""),
        )
    }

    // 오류 코드별 기본 문구 검증
    @Test
    fun knownErrorsDoNotDependOnCategoryHttpStatus() {
        assertEquals("존재하지 않는 사용자입니다.(404)", postErrorMessage(404, """{"error":{"code":"USER_NOT_FOUND"}}"""))
        assertEquals("존재하지 않는 카테고리입니다.(409)", postErrorMessage(409, """{"error":{"code":"CATEGORY_NOT_FOUND"}}"""))
    }

    // 오류 응답 누락·형식 오류 시 기본 문구 검증
    @Test
    fun malformedAndEmptyErrorsHaveReadableFallbacks() {
        assertEquals("게시글 작성에 실패했습니다. 다시 시도해 주세요.", postErrorMessage(500, "<html>error</html>"))
        assertEquals("게시글 작성에 실패했습니다. 다시 시도해 주세요.", postErrorMessage(404, null))
        assertEquals("로그인이 필요합니다. 다시 로그인해 주세요.", postErrorMessage(401, ""))
    }
}
