package com.example.deverytime_android2

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokens: SessionTokenStore = TokenStorage,
    private val loginApiProvider: () -> LoginApi,
) : Authenticator {

    private val gson = Gson()

    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        if (responseCount(response) >= 2) {
            if (response.code == 401) tokens.clear()
            return null
        }

        synchronized(this) {
            val failedAccessToken =
                response.request
                    .header("Authorization")
                    ?.removePrefix("Bearer ")

            val currentAccessToken =
                tokens.getAccessToken()

            if (
                !currentAccessToken.isNullOrBlank() &&
                currentAccessToken != failedAccessToken
            ) {
                return response.request.withToken(currentAccessToken)
            }

            val refreshToken =
                tokens.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                tokens.clear()
                return null
            }

            val reissueResponse =
                runCatching {
                    runBlocking {
                        loginApiProvider().reissueToken(
                            TokenReissueRequest(refreshToken),
                        )
                    }
                }.getOrNull() ?: return null

            val body = reissueResponse.body()
            val refreshedTokens = body?.data

            if (
                !reissueResponse.isSuccessful ||
                body?.success != true ||
                refreshedTokens == null
            ) {
                val errorCode =
                    runCatching {
                        gson.fromJson(
                            reissueResponse.errorBody()?.string(),
                            TokenReissueErrorResponse::class.java,
                        )?.error?.code
                    }.getOrNull()

                if (reissueResponse.code() == 401 || errorCode.isTokenAuthenticationError()) {
                    tokens.clear()
                }

                return null
            }

            tokens.saveTokens(refreshedTokens)

            return response.request.withToken(
                refreshedTokens.accessToken,
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
