package com.example.deverytime_android2

import retrofit2.Response

class CommentRepository(
    private val api: CommentApi = RetrofitClient.commentApi,
) {
    // 댓글 삭제 요청 전달
    suspend fun deleteComment(postId: Long, commentId: Long): Response<DeleteCommentResponse> {
        return api.deleteComment(postId, commentId)
    }

    // 댓글 목록 요청 전달
    suspend fun getComments(postId: Long): Response<CommentListResponse> {
        return api.getComments(postId)
    }

    // 댓글 작성 요청 전달
    suspend fun createComment(postId: Long, content: String): Response<CreateCommentResponse> {
        return api.createComment(postId, CreateCommentRequest(content))
    }
}
