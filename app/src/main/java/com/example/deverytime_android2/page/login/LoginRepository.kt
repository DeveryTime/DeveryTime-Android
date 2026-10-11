package com.example.deverytime_android2

import com.google.gson.Gson

sealed interface LoginResult {
    data class Success(
        val response: LoginResponse,
        val tokens: TokenData,
    ) : LoginResult

    data class Error(
        val message: String,
    ) : LoginResult
}

sealed interface TokenReissueResult {
    data class Success(
        val tokens: TokenData,
    ) : TokenReissueResult

    data class Error(
        val code: String?,
        val message: String,
    ) : TokenReissueResult
}

sealed interface LogoutResult {
    data object Success : LogoutResult

    data class Error(
        val message: String,
    ) : LogoutResult
}

class LoginRepository(
    private val loginApi: LoginApi = RetrofitClient.loginApi,
    private val authenticatedLoginApi: LoginApi = RetrofitClient.authenticatedLoginApi,
) {
    private val gson = Gson()

    suspend fun login(
        email: String,
        password: String,
    ): LoginResult {
        return try {
            val response =
                loginApi.login(
                    LoginRequest(
                        email = email,
                        password = password,
                    ),
                )
            val body = response.body()
            val tokens = body?.data

            if (
                response.isSuccessful &&
                body?.success == "true" &&
                tokens != null
            ) {
                LoginResult.Success(
                    response = body,
                    tokens = tokens,
                )
            } else {
                LoginResult.Error(
                    message =
                        if (response.isSuccessful) {
                            body?.message ?: "응답 데이터가 없습니다."
                        } else {
                            parseErrorMessage(response.errorBody()?.string())
                                ?: "로그인 실패: ${response.code()}"
                        },
                )
            }
        } catch (exception: Exception) {
            LoginResult.Error(
                message =
                    exception.message
                        ?: "네트워크 오류가 발생했습니다.",
            )
        }
    }

    suspend fun reissueToken(
        refreshToken: String,
    ): TokenReissueResult {
        return try {
            val response =
                loginApi.reissueToken(
                    TokenReissueRequest(refreshToken),
                )
            val body = response.body()
            val tokens = body?.data

            when {
                response.isSuccessful &&
                    body?.success == true &&
                    tokens != null ->
                    TokenReissueResult.Success(tokens)

                response.code() == 401 ->
                    TokenReissueResult.Error(
                        code = "TOKEN_INVALID",
                        message =
                            parseErrorMessage(response.errorBody()?.string())
                                ?: "유효하지 않은 토큰입니다.",
                    )

                else ->
                    TokenReissueResult.Error(
                        code = null,
                        message =
                            parseErrorMessage(response.errorBody()?.string())
                                ?: "토큰 재발급에 실패했습니다. (${response.code()})",
                    )
            }
        } catch (exception: Exception) {
            TokenReissueResult.Error(
                code = null,
                message =
                    exception.message
                        ?: "네트워크 오류가 발생했습니다.",
            )
        }
    }

    suspend fun logout(
        refreshToken: String,
    ): LogoutResult {
        return try {
            val response =
                authenticatedLoginApi.logout(
                    LogoutRequest(refreshToken),
                )
            val body = response.body()

            if (
                response.code() == 204 ||
                response.isSuccessful && body?.success == true
            ) {
                LogoutResult.Success
            } else {
                LogoutResult.Error(
                    message =
                        parseErrorMessage(response.errorBody()?.string())
                            ?: body?.message
                            ?: "로그아웃에 실패했습니다. (${response.code()})",
                )
            }
        } catch (exception: Exception) {
            LogoutResult.Error(
                message =
                    exception.message
                        ?: "네트워크 오류가 발생했습니다.",
            )
        }
    }

    private fun parseErrorMessage(
        errorBody: String?,
    ): String? {
        if (errorBody.isNullOrBlank()) {
            return null
        }

        return runCatching {
            val error =
                gson.fromJson(
                    errorBody,
                    ApiErrorResponse::class.java,
                )?.error

            error
                ?.details
                ?.mapNotNull { it.message }
                ?.takeIf { it.isNotEmpty() }
                ?.joinToString("\n")
                ?: error?.message
        }.getOrNull()
    }
}
