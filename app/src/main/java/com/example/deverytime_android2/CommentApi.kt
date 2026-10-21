package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApi {
    // 인증 토큰으로 댓글 삭제
    @PATCH("/api/posts/{postId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("postId") postId: Long,
        @Path("commentId") commentId: Long,
    ): Response<DeleteCommentResponse>

    // 게시글 댓글 목록 조회
    @GET("/api/posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Long): Response<CommentListResponse>

    // 인증 토큰으로 댓글 작성
    @POST("/api/posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: Long,
        @Body request: CreateCommentRequest,
    ): Response<CreateCommentResponse>
}
