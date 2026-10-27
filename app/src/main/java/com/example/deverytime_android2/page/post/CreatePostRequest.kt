package com.example.deverytime_android2

// 게시글 작성 요청
data class CreatePostRequest(
    val categoryId: Int,
    val title: String,
    val content: String,
) {
    // 공개 상태로 등록
    val status: String = "PUBLISHED"
}
