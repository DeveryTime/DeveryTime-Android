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
    fun defaultRequestOmitsOptionalCategoryAndParsesEmptyPage() = runBlocking {
        val api = api("""{"content":[],"page":0,"size":20,"totalElements":0,"totalPages":0}""") { request ->
            assertEquals("GET", request.method)
            assertEquals("/api/posts", request.url.encodedPath)
            assertEquals("0", request.url.queryParameter("page"))
            assertEquals("20", request.url.queryParameter("size"))
            assertEquals("latest", request.url.queryParameter("sort"))
            assertNull(request.url.queryParameter("categoryId"))
        }
        val page = PostRepository(api).getPosts().body()!!
        assertTrue(page.content.isEmpty())
        assertEquals(0, page.totalPages)
    }

    // 조회 조건·게시글 응답 검증
    @Test
    fun filteredPageUsesServerSortingAndParsesPostAndPagination() = runBlocking {
        val api = api("""{"content":[{"id":15,"title":"제목","category":"분실물","createdAt":"2026-08-04T12:30:00"}],"page":1,"size":20,"totalElements":21,"totalPages":2}""") { request ->
            assertEquals("1", request.url.queryParameter("page"))
            assertEquals("likes", request.url.queryParameter("sort"))
            assertEquals("8", request.url.queryParameter("categoryId"))
        }
        val page = PostRepository(api).getPosts(page = 1, sort = "likes", categoryId = 8L).body()!!
        assertEquals(PostListItem(15L, "제목", "분실물", "2026-08-04T12:30:00"), page.content.single())
        assertEquals(1, page.page)
        assertEquals(21L, page.totalElements)
        assertEquals(2, page.totalPages)
    }
}
