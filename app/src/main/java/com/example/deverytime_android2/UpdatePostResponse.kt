package com.example.deverytime_android2

// 게시글 수정 응답
data class UpdatePostResponse(
    val id: Long,
    val title: String,
    val updatedAt: String,
)
