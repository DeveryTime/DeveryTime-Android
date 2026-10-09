package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApi {

    // 목록 조회 (page: 0부터 / categoryId: null이면 전체)
    @GET("/api/posts")
    suspend fun getPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "latest",
        @Query("categoryId") categoryId: Long? = null,
    ): Response<PostListResponse>

    // 카테고리 목록 조회
    @GET("/api/categories")
    suspend fun getCategories(): Response<List<PostCategory>>

    // 게시글 등록
    @POST("/api/posts")
    suspend fun createPost(
        @Body request: CreatePostRequest
    ): Response<CreatePostResponse>

    // 기존 검색 API (GET 본문에 검색 조건 전달)
    @HTTP(
        method = "GET",
        path = "/api/posts/search",
        hasBody = true
    )
    suspend fun searchPosts(
        @Body request: SearchRequest
    ): SearchResponse
}
