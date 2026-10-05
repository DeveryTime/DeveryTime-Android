package com.example.deverytime_android2

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val loginApiProvider: () -> LoginApi,
) : Authenticator {

    private val gson = Gson()

    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        if (responseCount(response) >= 2) {
            return null
        }

        synchronized(this) {
            val failedAccessToken =
                response.request
                    .header("Authorization")
                    ?.removePrefix("Bearer ")

            val currentAccessToken =
                TokenStorage.getAccessToken()

            if (
                !currentAccessToken.isNullOrBlank() &&
                currentAccessToken != failedAccessToken
            ) {
                return response.request.withToken(currentAccessToken)
            }

            val refreshToken =
                TokenStorage.getRefreshToken()
                    ?: return null

            val reissueResponse =
                runCatching {
                    runBlocking {
                        loginApiProvider().reissueToken(
                            TokenReissueRequest(refreshToken),
                        )
                    }
                }.getOrNull() ?: return null

            val body = reissueResponse.body()
            val tokens = body?.data

            if (
                !reissueResponse.isSuccessful ||
                body?.success != true ||
                tokens == null
            ) {
                val errorCode =
                    runCatching {
                        gson.fromJson(
                            reissueResponse.errorBody()?.string(),
                            TokenReissueErrorResponse::class.java,
                        )?.error?.code
                    }.getOrNull()

                if (errorCode.isTokenAuthenticationError()) {
                    TokenStorage.clear()
                }

                return null
            }

            TokenStorage.saveTokens(tokens)

            return response.request.withToken(
                tokens.accessToken,
            )
        }
    }

    private fun String?.isTokenAuthenticationError(): Boolean {
        val code = this?.uppercase() ?: return false

        return "TOKEN" in code &&
            listOf("INVALID", "EXPIRED", "REVOKED").any(code::contains)
    }

    private fun Request.withToken(
        accessToken: String,
    ): Request {
        return newBuilder()
            .header(
                "Authorization",
                "Bearer $accessToken",
            )
            .build()
    }

    private fun responseCount(
        response: Response,
    ): Int {
        var count = 1
        var previousResponse = response.priorResponse

        while (previousResponse != null) {
            count++
            previousResponse = previousResponse.priorResponse
        }

        return count
    }
}
