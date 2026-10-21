package com.example.deverytime_android2

// 좋아요 등록 응답
data class CreateLikeResponse(
    val success: Boolean,
    val data: LikeData?,
    val message: String?,
)

// 좋아요·사용자 정보
data class LikeData(
    val postId: Long,
    val userId: Long,
    val liked: Boolean?,
)
