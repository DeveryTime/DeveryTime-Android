package com.example.deverytime_android2

// 좋아요 취소 응답
data class DeleteLikeResponse(
    val success: Boolean,
    val data: LikeData?,
    val message: String?,
)
