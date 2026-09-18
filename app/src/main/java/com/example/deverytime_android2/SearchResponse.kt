package com.example.deverytime_android2

data class SearchResponse(
    val success: Boolean,
    val data: SearchData,
    val message: String?
)

data class SearchData(
    val content: List<NetworkPost>,
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int
)

data class NetworkPost(
    val id: Int,
    val title: String,
    val categoryName: String,
    val createdAt: String
)