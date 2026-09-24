package com.example.deverytime_android2


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class TokenData(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class TokenReissueRequest(
    @SerializedName("refreshToken")
    val refreshToken: String,
)