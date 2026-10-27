package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchApi {
    @POST("/api/search")
    suspend fun search(@Body request: SearchRequest): Response<SearchResponse>
}
