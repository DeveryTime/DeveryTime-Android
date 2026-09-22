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
}