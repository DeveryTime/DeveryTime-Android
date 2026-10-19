package com.example.deverytime_android2

import retrofit2.Response

class LikeRepository(
    private val api: LikeApi = RetrofitClient.likeApi,
) {
    // 좋아요 등록 요청 전달
    suspend fun createLike(postId: Long): Response<CreateLikeResponse> {
        return api.createLike(postId)
    }

    // 좋아요 취소 요청 전달
    suspend fun deleteLike(postId: Long): Response<DeleteLikeResponse> {
        return api.deleteLike(postId)
    }
}
