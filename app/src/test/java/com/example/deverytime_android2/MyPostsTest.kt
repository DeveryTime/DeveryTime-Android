package com.example.deverytime_android2

import com.example.deverytime_android2.page.mypage.MyPostsUiState
import com.example.deverytime_android2.page.mypage.myPostsResult
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyPostsTest {
    private val emptyPage = """{"success":true,"data":{"content":[],"number":0,"size":20,"totalElements":0,"totalPages":0}}"""

    private fun retrofit(
        json: String,
        status: Int = 200,
        checkRequest: (Request) -> Unit = {},
    ): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            assertEquals("GET", request.method)
            assertEquals("/api/users/me/posts", request.url.encodedPath)
            assertNull(request.body)
            checkRequest(request)
            okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(status)
                .message("Test response")
                .body(json.toResponseBody("application/json".toMediaType()))
                .build()
        }.build()
        return Retrofit.Builder().baseUrl("https://example.test/")
            .client(client).addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun repository(retrofit: Retrofit) = MyPageRepository(
        api = retrofit.create(MyPageApi::class.java),
        usernameApi = retrofit.create(UsernameApi::class.java),
        profileImageReader = ProfileImageReader { error("Profile images are not used in this test") },
    )

    @Test
    fun defaultRequestUsesLatestAndParsesDocumentedPage() = runBlocking {
        val json = """{"success":true,"data":{
            "content":[{"id":15,"title":"제목","writerId":1,"category":"카테고리",
                "createdAt":"2026-08-04T12:30:00","likeCount":0}],
            "number":0,"size":20,"totalElements":1,"totalPages":1
        }}"""
        val retrofit = retrofit(json) { request ->
            assertEquals("0", request.url.queryParameter("page"))
            assertEquals("20", request.url.queryParameter("size"))
            assertEquals("createdAt,desc", request.url.queryParameter("sort"))
            assertNull(request.url.queryParameter("categoryId"))
        }
        val response = repository(retrofit).getMyPosts()
        val state = myPostsResult(response) as MyPostsUiState.Success
        assertEquals(
            MyPostsResponse(
                content = listOf(MyPostSummary(15L, "제목", 1L, "카테고리", "2026-08-04T12:30:00", 0)),
                page = 0, size = 20, totalElements = 1L, totalPages = 1,
            ),
            state.response,
        )
    }

    @Test
    fun apiDefaultsAlsoSendLatest() = runBlocking {
        val api = retrofit(emptyPage) { request ->
            assertEquals("0", request.url.queryParameter("page"))
            assertEquals("20", request.url.queryParameter("size"))
            assertEquals("createdAt,desc", request.url.queryParameter("sort"))
            assertNull(request.url.queryParameter("categoryId"))
        }.create(MyPageApi::class.java)
        assertEquals(MyPostsApiResponse(true, MyPostsResponse(emptyList(), 0, 20, 0L, 0)), api.getMyPosts().body())
    }

    @Test
    fun actualServerCategoryNameMapsToVisiblePostCategory() = runBlocking {
        val json = """{"success":true,"data":{
            "content":[{"id":15,"title":"제목","writerId":1,"categoryName":"일상",
                "createdAt":"2026-08-04T12:30:00","likeCount":0}],
            "number":0,"size":3,"totalElements":1,"totalPages":1
        }}"""
        val state = myPostsResult(repository(retrofit(json)).getMyPosts(size = 3)) as MyPostsUiState.Success
        assertEquals("일상", state.response.content.single().category)
        assertEquals("제목", state.response.content.single().title)
    }

    @Test
    fun convertsMyPageSortNamesAndPreservesOptionalCategory() = runBlocking {
        for ((sort, serverSort) in listOf(
            "latest" to "createdAt,desc",
            "views" to "viewCount,desc",
            "createdAt,desc" to "createdAt,desc",
            "viewCount,desc" to "viewCount,desc",
            "likes" to "likes",
        )) {
            val retrofit = retrofit(emptyPage) { request ->
                assertEquals("1", request.url.queryParameter("page"))
                assertEquals("3", request.url.queryParameter("size"))
                assertEquals(serverSort, request.url.queryParameter("sort"))
                assertEquals("4294967296", request.url.queryParameter("categoryId"))
            }
            val response = repository(retrofit).getMyPosts(page = 1, size = 3, sort = sort, categoryId = 4294967296L)
            assertTrue(response.isSuccessful)
        }
    }

    @Test
    fun acceptsEmptyContentAsSuccessfulEmptyList() = runBlocking {
        val response = repository(retrofit(emptyPage)).getMyPosts(size = 3)
        assertEquals(MyPostsUiState.Success(MyPostsResponse(emptyList(), 0, 20, 0L, 0)), myPostsResult(response))
    }

    @Test
    fun missingContentOrRequiredPostFieldsAreNotShownAsEmptyLists() = runBlocking {
        for (json in listOf(
            """{"page":0,"size":20,"totalElements":0,"totalPages":0}""",
            """{"success":true,"data":{}}""",
            """{"success":true,"data":{"content":[{"id":15,"title":"제목","createdAt":"2026-08-04T12:30:00"}],"number":0,"size":20,"totalElements":1,"totalPages":1}}""",
            """{"success":true,"data":null}""",
            """{"success":false,"data":{"content":[],"number":0,"size":20,"totalElements":0,"totalPages":0}}""",
        )) {
            val response = repository(retrofit(json)).getMyPosts()
            assertEquals(MyPostsUiState.Error("내가 쓴 글 응답을 확인할 수 없습니다."), myPostsResult(response))
        }
        assertEquals(
            MyPostsUiState.Error("내가 쓴 글 응답을 확인할 수 없습니다."),
            myPostsResult(Response.success<MyPostsApiResponse>(null)),
        )
    }

    @Test
    fun preservesLargeIdsAndNonzeroLikeCounts() = runBlocking {
        val json = """{"success":true,"data":{
            "content":[{"id":4294967296,"title":"제목","writerId":4294967297,"category":"전공",
                "createdAt":"2026-08-04T12:30:00","likeCount":12}],
            "number":1,"size":20,"totalElements":21,"totalPages":2
        }}"""
        val state = myPostsResult(repository(retrofit(json)).getMyPosts(page = 1)) as MyPostsUiState.Success
        val post = state.response.content.single()
        assertEquals(4294967296L, post.id)
        assertEquals(4294967297L, post.writerId)
        assertEquals(12, post.likeCount)
        assertEquals(1, state.response.page)
        assertEquals(2, state.response.totalPages)
    }

    @Test
    fun errorResponsesUseReadableMessages() = runBlocking {
        assertEquals(
            MyPostsUiState.Error("로그인이 필요합니다. 다시 로그인해 주세요."),
            myPostsResult(repository(retrofit("", status = 401)).getMyPosts()),
        )
        assertEquals(
            MyPostsUiState.Error("내가 쓴 글을 불러오지 못했습니다. 다시 시도해 주세요."),
            myPostsResult(repository(retrofit("<html>error</html>", status = 500)).getMyPosts()),
        )
        assertEquals(
            MyPostsUiState.Error("조회 권한이 없습니다."),
            myPostsResult(repository(retrofit("""{"error":{"message":"조회 권한이 없습니다."}}""", status = 403)).getMyPosts()),
        )
    }

    @Test
    fun parsesProvidedEmptyPageWithPageableMetadata() = runBlocking {
        val json = """{
            "success":true,
            "data":{
                "content":[],"empty":true,"first":false,"last":true,"number":3,"numberOfElements":0,
                "pageable":{
                    "offset":9,"pageNumber":3,"pageSize":3,"paged":true,
                    "sort":{"empty":false,"sorted":true,"unsorted":false},"unpaged":false
                },
                "size":3,"sort":{"empty":false,"sorted":true,"unsorted":false},
                "totalElements":3,"totalPages":1
            }
        }"""
        val state = myPostsResult(repository(retrofit(json)).getMyPosts(page = 3, size = 3))
        assertEquals(MyPostsUiState.Success(MyPostsResponse(emptyList(), 3, 3, 3L, 1)), state)
    }
}
