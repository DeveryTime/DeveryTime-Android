package com.example.deverytime_android2

import retrofit2.Response

class LoginRepository(
    private val loginApi: LoginApi = RetrofitClient.loginApi,
) {
    suspend fun login(
        email: String,
        password: String,
    ): Response<LoginResponse> {
        return loginApi.login(
            LoginRequest(
                email = email,
                password = password,
            ),
        )
    }
}