package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpApi {
    @POST("api/auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest,
    ): Response<SignUpResponse>

    @GET("api/auth/check-username")
    suspend fun checkUsername(
        @Query("username") username: String,
    ): Response<CheckUsernameResponse>

    @POST("api/auth/email-verifications")
    suspend fun sendEmailVerification(
        @Body request: EmailVerificationRequest,
    ): Response<EmailVerificationResponse>

    @POST("api/auth/email-verifications/verify")
    suspend fun verifyEmail(
        @Body request: EmailVerificationConfirmRequest,
    ): Response<EmailVerificationResponse>
}