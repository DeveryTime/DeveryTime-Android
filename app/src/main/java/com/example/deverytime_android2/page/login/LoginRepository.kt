package com.example.deverytime_android2

import retrofit2.Response

class LoginRepository(
    private val loginApi: LoginApi = RetrofitClient.loginApi,
    private val authenticatedLoginApi: LoginApi = RetrofitClient.authenticatedLoginApi,
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

    suspend fun reissueToken(
        refreshToken: String,
    ): Response<TokenReissueResponse> {
        return loginApi.reissueToken(
            TokenReissueRequest(refreshToken),
        )
    }

    suspend fun logout(
        refreshToken: String,
    ): Response<LogoutResponse> {
        return authenticatedLoginApi.logout(
            LogoutRequest(refreshToken),
        )
    }
}
