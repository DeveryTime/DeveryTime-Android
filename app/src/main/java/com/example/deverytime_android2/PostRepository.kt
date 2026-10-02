package com.example.deverytime_android2

class PostRepository(
    private val postApi: PostApi
) {

    suspend fun searchPosts(
        request: SearchRequest
    ): SearchResponse {
        return postApi.searchPosts(request)
    }
}