package com.example.deverytime_android2

import retrofit2.Response

class SignUpRepository(
    private val signUpApi: SignUpApi = RetrofitClient.signUpApi,
) {
    suspend fun signUp(
        request: SignUpRequest,
    ): Response<SignUpResponse> {
        return signUpApi.signUp(request)
    }

    suspend fun checkUsername(
        username: String,
    ): Response<CheckUsernameResponse> {
        return signUpApi.checkUsername(username)
    }

    suspend fun sendEmailVerification(
        email: String,
    ): Response<EmailVerificationResponse> {
        return signUpApi.sendEmailVerification(
            EmailVerificationRequest(email),
        )
    }

    suspend fun verifyEmail(
        email: String,
        code: String,
    ): Response<EmailVerificationResponse> {
        return signUpApi.verifyEmail(
            EmailVerificationConfirmRequest(
                email = email,
                code = code,
            ),
        )
    }
}