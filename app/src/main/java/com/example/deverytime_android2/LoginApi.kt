package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): Response<LoginResponse>

    @POST("api/auth/reissue")
    suspend fun reissueToken(
        @Body request: TokenReissueRequest,
    ): Response<TokenReissueResponse>
}
