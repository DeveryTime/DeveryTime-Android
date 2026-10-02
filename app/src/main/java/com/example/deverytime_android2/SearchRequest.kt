package com.example.deverytime_android2

data class SearchRequest(
    val keyword: String,
    val page: Int = 0,
    val size: Int = 10,
    val sort: String = "createdAt,desc"
)