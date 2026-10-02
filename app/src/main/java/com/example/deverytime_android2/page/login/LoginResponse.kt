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

data class TokenReissueResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: TokenData?,
)

data class TokenReissueErrorResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("error")
    val error: TokenReissueError?,
)

data class TokenReissueError(
    @SerializedName("code")
    val code: String?,
    @SerializedName("message")
    val message: String?,
)