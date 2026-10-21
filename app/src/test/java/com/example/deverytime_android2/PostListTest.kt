package com.example.deverytime_android2

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostListTest {
    // 서버 접속 없는 테스트용 API
    private fun api(json: String, checkRequest: (okhttp3.Request) -> Unit): PostApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            checkRequest(chain.request())
            okhttp3.Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(json.toResponseBody("application/json".toMediaType()))
                .build()
        }.build()
        return Retrofit.Builder().baseUrl("https://example.test/")
            .client(client).addConverterFactory(GsonConverterFactory.create())
            .build().create(PostApi::class.java)
    }

    // 기본 요청·빈 목록 응답 검증
    @Test
    fun defaultRequestOmitsOptionalSortAndCategoryAndParsesEmptyPage() = runBlocking {
        val api = api("""{"success":true,"data":{"content":[],"number":0,"size":20,"totalElements":0,"totalPages":0}}""") { request ->
            assertEquals("GET", request.method)
            assertEquals("/api/posts", request.url.encodedPath)
            assertEquals("0", request.url.queryParameter("page"))
            assertEquals("20", request.url.queryParameter("size"))
            assertNull(request.url.queryParameter("sort"))
            assertNull(request.url.queryParameter("categoryId"))
        }
        val result = PostRepository(api).getPosts().body()!!
        assertTrue(result.success)
        val page = result.data!!
        assertTrue(page.content.isEmpty())
        assertEquals(0, page.page)
        assertEquals(20, page.size)
        assertEquals(0, page.totalPages)
    }

    // 조회 조건·게시글 응답 검증
    @Test
    fun filteredPageUsesServerSortingAndParsesPostAndPagination() = runBlocking {
        val api = api("""{"success":true,"data":{"content":[{"id":15,"title":"제목","categoryName":"분실물","createdAt":"2026-08-04T12:30:00","likeCount":7}],"number":1,"size":20,"totalElements":21,"totalPages":2,"empty":false,"first":false,"last":true,"numberOfElements":1,"pageable":{"offset":20,"pageNumber":1,"pageSize":20,"paged":true,"sort":{"empty":false,"sorted":true,"unsorted":false},"unpaged":false},"sort":{"empty":false,"sorted":true,"unsorted":false}}}""") { request ->
            assertEquals("1", request.url.queryParameter("page"))
            assertEquals("viewCount,desc", request.url.queryParameter("sort"))
            assertEquals("8", request.url.queryParameter("categoryId"))
        }
        val result = PostRepository(api).getPosts(page = 1, sort = "views", categoryId = 8L).body()!!
        assertTrue(result.success)
        val page = result.data!!
        assertEquals(PostListItem(15L, "제목", "분실물", "2026-08-04T12:30:00", 7), page.content.single())
        assertEquals(1, page.page)
        assertEquals(21L, page.totalElements)
        assertEquals(2, page.totalPages)
    }

    @Test
    fun popularPageUsesLikeCountSortAndParsesUpdatedResponse() = runBlocking {
        val api = api("""{
            "success":true,
            "data":{
                "content":[{"id":13,"title":"더미 게시글 제목","categoryName":"전공",
                    "createdAt":"2026-10-10T13:57:25.68011","likeCount":0}],
                "empty":false,"first":true,"last":false,"number":0,"numberOfElements":1,
                "pageable":{"offset":0,"pageNumber":0,"pageSize":1,"paged":true,
                    "sort":{"empty":false,"sorted":true,"unsorted":false},"unpaged":false},
                "size":1,"sort":{"empty":false,"sorted":true,"unsorted":false},
                "totalElements":11,"totalPages":11
            }
        }""") { request ->
            assertEquals("0", request.url.queryParameter("page"))
            assertEquals("1", request.url.queryParameter("size"))
            assertEquals("likeCount,desc", request.url.queryParameter("sort"))
            assertEquals("3", request.url.queryParameter("categoryId"))
        }
        val result = PostRepository(api).getPosts(size = 1, sort = "likes", categoryId = 3L).body()!!
        assertTrue(result.success)
        val page = result.data!!
        assertEquals(PostListItem(13L, "더미 게시글 제목", "전공", "2026-10-10T13:57:25.68011", 0), page.content.single())
        assertEquals(0, page.page)
        assertEquals(1, page.size)
        assertEquals(11L, page.totalElements)
        assertEquals(11, page.totalPages)
    }

    @Test
    fun latestPageUsesCreatedAtSort() = runBlocking {
        val api = api("""{"success":true,"data":{"content":[],"number":0,"size":20,"totalElements":0,"totalPages":0}}""") { request ->
            assertEquals("createdAt,desc", request.url.queryParameter("sort"))
        }
        PostRepository(api).getPosts(sort = "latest")
        Unit
    }

    @Test
    fun explicitSortExpressionsArePassedThrough() = runBlocking {
        for (sort in listOf("createdAt,desc", "viewCount,desc", "likeCount,desc", "createdAt,asc")) {
            val api = api("""{"success":true,"data":{"content":[],"number":0,"size":20,"totalElements":0,"totalPages":0}}""") { request ->
                assertEquals(sort, request.url.queryParameter("sort"))
                assertEquals("4294967296", request.url.queryParameter("categoryId"))
            }
            PostRepository(api).getPosts(sort = sort, categoryId = 4294967296L)
        }
    }

    @Test
    fun apiDefaultRequestOmitsSorting() = runBlocking {
        val api = api("""{"success":true,"data":{"content":[],"number":0,"size":20,"totalElements":0,"totalPages":0}}""") { request ->
            assertEquals("0", request.url.queryParameter("page"))
            assertEquals("20", request.url.queryParameter("size"))
            assertNull(request.url.queryParameter("sort"))
            assertNull(request.url.queryParameter("categoryId"))
        }
        api.getPosts()
        Unit
    }
}
