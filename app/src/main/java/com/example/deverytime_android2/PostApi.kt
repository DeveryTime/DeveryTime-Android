package com.example.deverytime_android2

import retrofit2.http.Body
import retrofit2.http.HTTP

interface PostApi {

    @HTTP(
        method = "GET",
        path = "/api/posts/search",
        hasBody = true
    )
    suspend fun searchPosts(
        @Body request: SearchRequest
    ): SearchResponse
}