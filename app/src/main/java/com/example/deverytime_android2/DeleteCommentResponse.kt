package com.example.deverytime_android2

// 댓글 삭제 응답
data class DeleteCommentResponse(
    val success: Boolean,
    val data: Unit?,
    val message: String?,
)
