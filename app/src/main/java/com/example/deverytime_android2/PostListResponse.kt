package com.example.deverytime_android2

// 게시글 목록 항목
data class PostListItem(
    val id: Long,
    val title: String,
    val category: String,
    val createdAt: String,
)

// 목록 조회 응답·페이지 정보
data class PostListResponse(
    val content: List<PostListItem>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)
