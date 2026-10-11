package com.example.deverytime_android2

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val baseUrl = BuildConfig.BASE_URL.toHttpUrl()

    private val certificatePinner =
        CertificatePinner.Builder()
            // 중간 CA (Let's Encrypt YE1, 2028-09 까지)
            .add(
                baseUrl.host,
                "sha256/brzvtCELCIZUo4sD/qPX0ccRtPsd3DY6RfmxpOU9oB4=",
            )
            // 백업 (ISRG Root YE, 2032-09 까지)
            .add(
                baseUrl.host,
                "sha256/sCkq5UWXjg+7mKu9lMhhYF5bGLsy7VI/UNW3tccdR7w=",
            )
            .build()

    private val publicRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .certificatePinner(certificatePinner)
                    .build(),
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginApi: LoginApi by lazy {
        publicRetrofit.create(LoginApi::class.java)
    }

    val signUpApi: SignUpApi by lazy {
        publicRetrofit.create(SignUpApi::class.java)
    }

    val usernameApi: UsernameApi by lazy {
        publicRetrofit.create(UsernameApi::class.java)
    }

    private val authenticatedRetrofit: Retrofit by lazy {
        val client =
            OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .addInterceptor(AuthInterceptor())
                .authenticator(
                    TokenAuthenticator {
                        loginApi
                    },
                )
                .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val postApi: PostApi by lazy {
        authenticatedRetrofit.create(PostApi::class.java)
    }
    val myPageApi: MyPageApi by lazy {
        authenticatedRetrofit.create(MyPageApi::class.java)
    }

    val authenticatedLoginApi: LoginApi by lazy {
        authenticatedRetrofit.create(LoginApi::class.java)
    }
}
