package com.example.deverytime_android2

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// 회원가입·마이페이지에서 공통으로 사용하는 기존 아이디 중복 확인 API
interface UsernameApi {
    @GET("api/auth/check-username")
    suspend fun checkUsername(
        @Query("username") username: String,
    ): Response<CheckUsernameResponse>
}
