package com.example.deverytime_android2


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: TokenData,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: String
)