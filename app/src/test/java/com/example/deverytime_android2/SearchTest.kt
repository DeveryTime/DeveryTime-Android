package com.example.deverytime_android2

import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchTest {
    private fun api(
        json: String,
        status: Int = 200,
        checkRequest: (Request) -> Unit = {},
    ): SearchApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            assertEquals("POST", request.method)
            assertEquals("/api/search", request.url.encodedPath)
            assertNull(request.url.query)
            checkRequest(request)
            okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(status)
                .message("Test response")
                .body(json.toResponseBody("application/json".toMediaType()))
                .build()
        }.build()
        return Retrofit.Builder()
            .baseUrl("https://example.test/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)
    }

    @Test
    fun sendsTrimmedKeywordAndDefaultsAndParsesUsersAndPosts() = runBlocking {
        val json = """{
            "success":true,
            "data":{
                "users":[{"id":4294967296,"username":"최지민","nickname":"지민","profileImageUrl":"https://example.test/5.jpg"}],
                "posts":{
                    "content":[{"id":4294967297,"title":"대회 안내","writerId":4294967296,"categoryName":"일상","createdAt":"2026-08-25T19:41:54","likeCount":0}],
                    "totalElements":1,"totalPages":1,"number":0,"size":10
                }
            }
        }"""
        val api = api(json) { request ->
            val buffer = Buffer()
            request.body!!.writeTo(buffer)
            val body = JsonParser.parseString(buffer.readUtf8()).asJsonObject
            assertEquals(4, body.size())
            assertEquals("대회", body["keyword"].asString)
            assertEquals(0, body["page"].asInt)
            assertEquals(10, body["size"].asInt)
            assertEquals("createdAt,desc", body["sort"].asString)
            assertEquals("application/json; charset=UTF-8", request.body!!.contentType().toString())
        }
        val response = SearchRepository(api).search("  대회\n")
        assertEquals(200, response.code())
        val result = response.body()!!
        assertTrue(result.success)
        val data = result.data!!
        assertEquals(SearchUserResult(4294967296L, "최지민", "지민", "https://example.test/5.jpg"), data.users!!.single())
        assertEquals(
            PostListItem(4294967297L, "대회 안내", "일상", "2026-08-25T19:41:54", writerId = 4294967296L),
            data.posts!!.content.single(),
        )
        assertEquals(0, data.posts.page)
        assertEquals(1L, data.posts.totalElements)
        assertEquals(1, data.posts.totalPages)
    }

    @Test
    fun nullableUserNamesHaveNonNullDisplayNames() = runBlocking {
        val json = """{"success":true,"data":{
            "users":[
                {"id":1,"username":"최지민","nickname":null},
                {"id":2,"username":null,"nickname":"지민"},
                {"id":3,"username":null,"nickname":null},
                {"id":4,"username":" ","nickname":" "}
            ],
            "posts":{"content":[],"totalElements":0,"totalPages":0,"number":0,"size":10}
        }}"""
        val users = SearchRepository(api(json)).search("지민").body()!!.data!!.users!!
        assertEquals(listOf("최지민", "지민", "사용자", "사용자"), users.map { it.displayName })
        assertNull(users[0].nickname)
        assertNull(users[1].username)
    }

    @Test
    fun omittedUserNamesHaveSafeFallback() = runBlocking {
        val json = """{"success":true,"data":{
            "users":[{"id":5}],
            "posts":{"content":[],"totalElements":0,"totalPages":0,"number":0,"size":10}
        }}"""
        val user = SearchRepository(api(json)).search("지민").body()!!.data!!.users!!.single()
        assertEquals("사용자", user.displayName)
        assertNull(user.username)
        assertNull(user.nickname)
        assertNull(user.profileImageUrl)
    }

    @Test
    fun sendsNextPageAndParsesNonzeroPageNumber() = runBlocking {
        val json = """{"success":true,"data":{"users":[],"posts":{
            "content":[],"totalElements":21,"totalPages":3,"number":1,"size":10
        }}}"""
        val api = api(json) { request ->
            val buffer = Buffer()
            request.body!!.writeTo(buffer)
            val body = JsonParser.parseString(buffer.readUtf8()).asJsonObject
            assertEquals("대회", body["keyword"].asString)
            assertEquals(1, body["page"].asInt)
            assertEquals(10, body["size"].asInt)
            assertEquals("createdAt,desc", body["sort"].asString)
        }
        val page = SearchRepository(api).search("대회", page = 1).body()!!.data!!.posts!!
        assertEquals(1, page.page)
        assertEquals(21L, page.totalElements)
        assertEquals(3, page.totalPages)
    }

    @Test
    fun emptyUsersAndPostsAreSuccessfulEmptyResults() = runBlocking {
        val json = """{"success":true,"data":{"users":[],"posts":{
            "content":[],"totalElements":0,"totalPages":0,"number":0,"size":10
        }}}"""
        val result = SearchRepository(api(json)).search("없는검색어").body()!!
        assertTrue(result.success)
        val data = result.data!!
        assertTrue(data.users!!.isEmpty())
        assertTrue(data.posts!!.content.isEmpty())
        assertEquals(0, data.posts.totalPages)
    }

    @Test
    fun emptyUnauthorizedBodyUsesLoginMessage() = runBlocking {
        val response = SearchRepository(api("", status = 401)).search("대회")
        assertFalse(response.isSuccessful)
        assertEquals(
            "로그인이 필요한 서비스입니다.",
            searchErrorMessage(response.code(), response.errorBody()?.string()),
        )
    }

    @Test
    fun unauthorizedJsonUsesServerMessage() = runBlocking {
        val json = """{"success":false,"error":{"code":"UNAUTHORIZED","message":"다시 로그인해 주세요."}}"""
        val response = SearchRepository(api(json, status = 401)).search("대회")
        assertEquals("다시 로그인해 주세요.", searchErrorMessage(response.code(), response.errorBody()?.string()))
    }

    @Test
    fun validationErrorsUseServerMessageWithOrWithoutDetails() = runBlocking {
        val bodies = listOf(
            """{"success":false,"status":400,"error":{"code":"VALIDATION_ERROR","message":"필수 입력 항목입니다."}}""",
            """{"success":false,"error":{"code":"VALIDATION_ERROR","message":"필수 입력 항목입니다.","details":[{"field":"keyword","message":"필수 입력 항목입니다."}]}}""",
        )
        for (json in bodies) {
            val response = SearchRepository(api(json, status = 400)).search("대회")
            assertEquals("필수 입력 항목입니다.", searchErrorMessage(response.code(), response.errorBody()?.string()))
        }
    }

    @Test
    fun missingAndMalformedErrorsHaveFallbackMessages() {
        assertEquals("로그인이 필요한 서비스입니다.", searchErrorMessage(401, "<html>error</html>"))
        assertEquals("입력값이 올바르지 않습니다.", searchErrorMessage(400, null))
        assertEquals("검색에 실패했습니다. (HTTP 500)", searchErrorMessage(500, "<html>error</html>"))
    }

    @Test
    fun blankKeywordIsRejectedWithoutApiCall() {
        val api = object : SearchApi {
            override suspend fun search(request: SearchRequest): Response<SearchResponse> =
                error("Blank keyword must not call the API")
        }
        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking { SearchRepository(api).search(" \n\t ") }
        }
        assertEquals("검색어를 입력해 주세요.", exception.message)
    }

    private fun postApi(pages: List<String>, requestedPages: MutableList<Int>): PostApi {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            assertEquals("/api/posts", request.url.encodedPath)
            assertEquals("createdAt,desc", request.url.queryParameter("sort"))
            assertNull(request.url.queryParameter("categoryId"))
            val page = request.url.queryParameter("page")!!.toInt()
            requestedPages += page
            okhttp3.Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("Test response")
                .body(pages[page].toResponseBody("application/json".toMediaType()))
                .build()
        }.build()
        return Retrofit.Builder()
            .baseUrl("https://example.test/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)
    }

    private fun postPage(page: Int, totalPages: Int, vararg posts: Pair<Long, Long>): String {
        val content = posts.joinToString(",") { (id, writerId) ->
            """{"id":$id,"title":"글$id","writerId":$writerId,"categoryName":"일상","createdAt":"2026-08-25T19:41:54","likeCount":0}"""
        }
        return """{"success":true,"data":{"content":[$content],"number":$page,"size":50,"totalElements":0,"totalPages":$totalPages}}"""
    }

    @Test
    fun findsPostsByWritersAcrossPagesUpToLimit() = runBlocking {
        val requestedPages = mutableListOf<Int>()
        val pages = listOf(
            postPage(0, 3, 1L to 5L, 2L to 9L, 3L to 5L),
            postPage(1, 3, 4L to 7L, 5L to 5L, 6L to 5L),
            postPage(2, 3, 7L to 7L),
        )
        val repository = SearchRepository(api("{}"), postApi(pages, requestedPages))
        val found = repository.findPostsByWriters(setOf(5L, 7L), perWriter = 3)
        assertEquals(listOf(1L, 3L, 5L), found.getValue(5L).map { it.id })
        assertEquals(listOf(4L, 7L), found.getValue(7L).map { it.id })
        assertEquals(listOf(0, 1, 2), requestedPages)
    }

    @Test
    fun stopsScanningWhenEveryWriterHasEnoughPosts() = runBlocking {
        val requestedPages = mutableListOf<Int>()
        val pages = listOf(postPage(0, 5, 1L to 5L, 2L to 5L), postPage(1, 5, 3L to 5L))
        val repository = SearchRepository(api("{}"), postApi(pages, requestedPages))
        val found = repository.findPostsByWriters(setOf(5L), perWriter = 2)
        assertEquals(listOf(1L, 2L), found.getValue(5L).map { it.id })
        assertEquals(listOf(0), requestedPages)
    }
}
