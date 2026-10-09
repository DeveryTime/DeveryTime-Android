package com.example.deverytime_android2

// 게시글 상세 응답
data class PostDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val status: String,
    val viewCount: Int,
    val writer: PostWriter,
    val category: PostDetailCategory,
    val images: List<PostImage>,
    val createdAt: String,
    val updatedAt: String?,
)

// 작성자 정보
data class PostWriter(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String?,
)

// 상세 카테고리 정보
data class PostDetailCategory(
    val id: Long,
    val name: String,
)

// 첨부 이미지·표시 순서
data class PostImage(
    val id: Long,
    val imageUrl: String,
    val sortOrder: Int,
)
