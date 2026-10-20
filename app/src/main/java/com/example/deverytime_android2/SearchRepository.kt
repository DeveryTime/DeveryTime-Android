package com.example.deverytime_android2

import retrofit2.Response

class SearchRepository(
    private val api: SearchApi = RetrofitClient.searchApi,
) {
    suspend fun search(
        keyword: String,
        page: Int = 0,
        size: Int = 10,
        sort: String = "createdAt,desc",
    ): Response<SearchResponse> {
        val trimmedKeyword = keyword.trim()
        require(trimmedKeyword.isNotEmpty()) { "검색어를 입력해 주세요." }
        return api.search(SearchRequest(trimmedKeyword, page, size, sort))
    }
}
