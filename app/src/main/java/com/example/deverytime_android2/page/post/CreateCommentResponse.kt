package com.example.deverytime_android2

// 댓글 작성 응답
data class CreateCommentResponse(
    val success: Boolean,
    val data: CommentData?,
    val message: String?,
)

// 등록된 댓글 정보
data class CommentData(
    val id: Long,
    val postId: Long,
    val userId: Long,
    val content: String,
    val createdAt: String,
)
