package com.example.deverytime_android2

import com.google.gson.annotations.SerializedName

// 게시글 목록 항목
data class PostListItem(
    val id: Long,
    val title: String,
    @SerializedName("categoryName") val category: String,
    val createdAt: String,
    val likeCount: Int = 0,
)

// 목록 조회 응답
data class PostListResponse(
    val success: Boolean,
    val data: PostListPage?,
)

// data 내부의 게시글 목록·페이지 정보
data class PostListPage(
    val content: List<PostListItem>,
    @SerializedName("number") val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)
