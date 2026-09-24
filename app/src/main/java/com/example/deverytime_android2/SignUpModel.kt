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

data class CheckUsernameResponse(
    val success: Boolean,
    val data: Any?,
    val message: String,
)

data class ApiErrorResponse(
    val success: Boolean,
    val error: ApiError?,
)

data class ApiError(
    val code: String?,
    val message: String?,
)

data class EmailVerificationRequest(
    val email: String,
)

data class EmailVerificationConfirmRequest(
    val email: String,
    val code: String,
)

data class EmailVerificationResponse(
    val success: Boolean,
    val data: Any?,
    val message: String,
)