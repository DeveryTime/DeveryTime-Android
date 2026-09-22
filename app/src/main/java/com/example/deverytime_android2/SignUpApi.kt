package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {
    @POST("api/auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest,
    ): Response<SignUpResponse>
}