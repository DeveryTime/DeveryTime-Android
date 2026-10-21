package com.example.deverytime_android2

// 기존 명세의 작성 응답 (실제 서버 형식 확인 필요)
data class CreatePostResponse(
    val id: Int,
    val title: String,
    val status: String,
    val createdAt: String,
)
