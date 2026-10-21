package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.Query

interface PostApi {

    // 인증 토큰으로 게시글 삭제 (요청 본문 없음)
    @DELETE("/api/posts/{id}")
    suspend fun deletePost(@Path("id") id: Long): Response<Unit>

    // 제목·본문 수정
    @PUT("/api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Long,
        @Body request: UpdatePostRequest,
    ): Response<UpdatePostResponse>

    // 게시글 ID로 상세 조회
    @GET("/api/posts/{id}")
    suspend fun getPost(@Path("id") id: Long): Response<PostDetailResponse>

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

}
