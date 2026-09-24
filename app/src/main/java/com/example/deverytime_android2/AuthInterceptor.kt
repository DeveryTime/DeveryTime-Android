package com.example.deverytime_android2

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = TokenStorage.getAccessToken()

        val request =
            if (accessToken.isNullOrBlank()) {
                chain.request()
            } else {
                chain.request()
                    .newBuilder()
                    .header(
                        "Authorization",
                        "Bearer $accessToken",
                    )
                    .build()
            }

        return chain.proceed(request)
    }
}