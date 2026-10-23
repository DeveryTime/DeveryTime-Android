package com.example.deverytime_android2

import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Response

class SessionRestoreTest {
    private class SavedTokens(
        accessToken: String? = "saved-access",
        refreshToken: String? = "saved-refresh",
    ) : SessionTokenStore {
        var access = accessToken
        var refresh = refreshToken
        var cleared = false

        override fun getAccessToken() = access
        override fun getRefreshToken() = refresh
        override fun saveTokens(tokens: TokenData) {
            access = tokens.accessToken
            refresh = tokens.refreshToken
        }
        override fun clear() {
            access = null
            refresh = null
            cleared = true
        }
    }

    private class ReissueApi(
        val response: (TokenReissueRequest) -> Response<TokenReissueResponse> = {
            Response.success(TokenReissueResponse(true, TokenData("new-access", "new-refresh")))
        },
    ) : LoginApi {
        var calls = 0

        override suspend fun reissueToken(request: TokenReissueRequest): Response<TokenReissueResponse> {
            calls++
            return response(request)
        }
        override suspend fun login(request: LoginRequest): Response<LoginResponse> = error("Unexpected login")
        override suspend fun logout(request: LogoutRequest): Response<LogoutResponse> = error("Unexpected logout")
    }

    private fun unauthorized(
        accessToken: String? = "saved-access",
        priorResponse: okhttp3.Response? = null,
    ): okhttp3.Response {
        val request = Request.Builder().url("https://example.test/api/users/me").apply {
            if (accessToken != null) header("Authorization", "Bearer $accessToken")
        }.build()
        return okhttp3.Response.Builder().request(request)
            .protocol(Protocol.HTTP_1_1).code(401).message("Unauthorized")
            .priorResponse(priorResponse).build()
    }

    private fun error(status: Int, json: String) = Response.error<TokenReissueResponse>(
        status, json.toResponseBody("application/json".toMediaType()),
    )

    @Test
    fun savedAccessTokenStartsAtHomeAfterRelaunch() {
        val tokens = SavedTokens(refreshToken = null)
        assertEquals(Screen.Main1.route, sessionStartDestination(tokens))
        assertFalse(tokens.cleared)
    }

    @Test
    fun refreshOnlySessionCanStartAtHomeAndRecoverAccessToken() {
        val tokens = SavedTokens(accessToken = null)
        val api = ReissueApi { request ->
            assertEquals("saved-refresh", request.refreshToken)
            Response.success(TokenReissueResponse(true, TokenData("new-access", "new-refresh")))
        }
        assertEquals(Screen.Main1.route, sessionStartDestination(tokens))
        val retry = TokenAuthenticator(tokens) { api }.authenticate(null, unauthorized(accessToken = null))
        assertEquals("Bearer new-access", retry?.header("Authorization"))
        assertEquals("new-access", tokens.access)
        assertEquals("new-refresh", tokens.refresh)
    }

    @Test
    fun missingOrClearedTokensDoNotRestoreLogin() {
        assertEquals(Screen.OnBoard1.route, sessionStartDestination(SavedTokens(null, null)))
        assertEquals(Screen.OnBoard1.route, sessionStartDestination(SavedTokens("", " ")))
        val tokens = SavedTokens()
        tokens.clear()
        assertEquals(Screen.OnBoard1.route, sessionStartDestination(tokens))
    }

    @Test
    fun expiredAccessTokenIsRefreshedAndRetried() {
        val tokens = SavedTokens()
        val api = ReissueApi()
        val retry = TokenAuthenticator(tokens) { api }.authenticate(null, unauthorized())
        assertEquals("Bearer new-access", retry?.header("Authorization"))
        assertEquals(1, api.calls)
        assertEquals("new-access", tokens.access)
        assertEquals("new-refresh", tokens.refresh)
        assertFalse(tokens.cleared)
    }

    @Test
    fun concurrentRequestsReuseAlreadyRefreshedAccessToken() {
        val tokens = SavedTokens(accessToken = "already-refreshed")
        val api = ReissueApi()
        val retry = TokenAuthenticator(tokens) { api }.authenticate(null, unauthorized())
        assertEquals("Bearer already-refreshed", retry?.header("Authorization"))
        assertEquals(0, api.calls)
    }

    @Test
    fun invalidRefreshTokenClearsSessionEvenWithoutErrorCode() {
        val tokens = SavedTokens()
        val api = ReissueApi { error(401, "{}") }
        assertNull(TokenAuthenticator(tokens) { api }.authenticate(null, unauthorized()))
        assertNull(tokens.access)
        assertNull(tokens.refresh)
        assertEquals(Screen.OnBoard1.route, sessionStartDestination(tokens))
    }

    @Test
    fun expiredRefreshTokenCodeClearsSession() {
        val tokens = SavedTokens()
        val api = ReissueApi { error(400, """{"error":{"code":"TOKEN_EXPIRED"}}""") }
        assertNull(TokenAuthenticator(tokens) { api }.authenticate(null, unauthorized()))
        assertEquals(Screen.OnBoard1.route, sessionStartDestination(tokens))
    }

    @Test
    fun expiredAccessTokenWithoutRefreshTokenClearsSession() {
        val tokens = SavedTokens(refreshToken = null)
        val api = ReissueApi()
        assertNull(TokenAuthenticator(tokens) { api }.authenticate(null, unauthorized()))
        assertEquals(Screen.OnBoard1.route, sessionStartDestination(tokens))
        assertEquals(0, api.calls)
    }

    @Test
    fun transientServerFailureKeepsSavedSession() {
        val tokens = SavedTokens()
        val api = ReissueApi { error(500, """{"error":{"code":"INTERNAL_SERVER_ERROR"}}""") }
        assertNull(TokenAuthenticator(tokens) { api }.authenticate(null, unauthorized()))
        assertFalse(tokens.cleared)
        assertEquals("saved-access", tokens.access)
        assertEquals("saved-refresh", tokens.refresh)
    }

    @Test
    fun offlineRefreshKeepsSavedSession() {
        val tokens = SavedTokens()
        val api = ReissueApi { throw IOException("Offline") }
        assertNull(TokenAuthenticator(tokens) { api }.authenticate(null, unauthorized()))
        assertFalse(tokens.cleared)
        assertEquals("saved-refresh", tokens.refresh)
    }

    @Test
    fun rejectedRefreshedTokenStopsRetryingAndClearsSession() {
        val tokens = SavedTokens(accessToken = "new-access", refreshToken = "new-refresh")
        val api = ReissueApi()
        val response = unauthorized(accessToken = "new-access", priorResponse = unauthorized())
        assertNull(TokenAuthenticator(tokens) { api }.authenticate(null, response))
        assertEquals(Screen.OnBoard1.route, sessionStartDestination(tokens))
        assertEquals(0, api.calls)
    }
}
