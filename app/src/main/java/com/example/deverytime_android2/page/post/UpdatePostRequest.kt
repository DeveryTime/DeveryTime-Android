package com.example.deverytime_android2

// 로그인한 사용자 ID·수정 내용
data class UpdatePostRequest(
    val userId: Long,
    val title: String,
    val content: String,
)
