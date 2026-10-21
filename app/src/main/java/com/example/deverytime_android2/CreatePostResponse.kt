package com.example.deverytime_android2

// 게시글 작성 응답 (data: 등록된 게시글 ID)
data class CreatePostResponse(
    val success: Boolean,
    val data: Long?,
)
