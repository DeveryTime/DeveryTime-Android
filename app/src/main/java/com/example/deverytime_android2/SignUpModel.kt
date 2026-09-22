package com.example.deverytime_android2

data class SignUpRequest(
    val schoolNumber: String,
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val passwordConfirm: String,
)

data class SignUpResponse(
    val success: Boolean,
    val data: Any?,
    val message: String,
)