package com.example.deverytime_android2

// 저장된 세션으로 시작하고, 만료된 액세스 토큰은 첫 API 요청에서 갱신
internal fun sessionStartDestination(tokens: SessionTokenStore = TokenStorage): String {
    val hasSession = !tokens.getAccessToken().isNullOrBlank() || !tokens.getRefreshToken().isNullOrBlank()
    return if (hasSession) Screen.Main1.route else Screen.OnBoard1.route
}
