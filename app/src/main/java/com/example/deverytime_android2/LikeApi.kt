package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeApi {
    // 인증 토큰으로 좋아요 등록
    @POST("/api/posts/{postId}/likes")
    suspend fun createLike(@Path("postId") postId: Long): Response<CreateLikeResponse>

    // 인증 토큰으로 좋아요 취소
    @DELETE("/api/posts/{postId}/likes")
    suspend fun deleteLike(@Path("postId") postId: Long): Response<DeleteLikeResponse>
}
