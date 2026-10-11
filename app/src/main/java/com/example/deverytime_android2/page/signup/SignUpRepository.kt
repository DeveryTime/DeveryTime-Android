package com.example.deverytime_android2

import retrofit2.Response

class SignUpRepository(
    private val signUpApi: SignUpApi = RetrofitClient.signUpApi,
    private val usernameApi: UsernameApi = RetrofitClient.usernameApi,
) {
    suspend fun signUp(
        request: SignUpRequest,
    ): Response<SignUpResponse> {
        return signUpApi.signUp(request)
    }

    suspend fun checkUsername(
        username: String,
    ): Response<CheckUsernameResponse> {
        return usernameApi.checkUsername(username)
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
