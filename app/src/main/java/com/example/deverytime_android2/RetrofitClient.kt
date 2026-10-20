package com.example.deverytime_android2

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://3.36.87.221/"

    private val publicRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginApi: LoginApi by lazy {
        publicRetrofit.create(LoginApi::class.java)
    }

    val signUpApi: SignUpApi by lazy {
        publicRetrofit.create(SignUpApi::class.java)
    }

    private val authenticatedRetrofit: Retrofit by lazy {
        val client =
            OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .authenticator(
                    TokenAuthenticator {
                        loginApi
                    },
                )
                .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val postApi: PostApi by lazy {
        authenticatedRetrofit.create(PostApi::class.java)
    }

    // 좋아요 API에 기존 인증 처리 적용
    val likeApi: LikeApi by lazy {
        authenticatedRetrofit.create(LikeApi::class.java)
    }

    // 통합 검색에도 로그인 토큰·갱신 처리 적용
    val searchApi: SearchApi by lazy {
        authenticatedRetrofit.create(SearchApi::class.java)
    }
}
