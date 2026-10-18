package com.example.deverytime_android2

// 댓글 목록 응답
data class CommentListResponse(
    val success: Boolean,
    val data: List<CommentListItem>?,
)

// 댓글·작성자 정보
data class CommentListItem(
    val id: Long,
    val userId: Long,
    val authorName: String,
    val profileImageUrl: String?,
    val content: String,
    val createdAt: String,
)
