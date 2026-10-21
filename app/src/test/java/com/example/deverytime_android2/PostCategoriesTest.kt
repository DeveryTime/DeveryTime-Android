package com.example.deverytime_android2

import com.google.gson.JsonSyntaxException
import java.io.IOException
import javax.net.ssl.SSLHandshakeException
import org.junit.Assert.assertEquals
import org.junit.Test

class PostCategoriesTest {
    // 로그인 오류 문구·HTTP 상태 검증
    @Test
    fun unauthorizedResponseShowsActualServerMessageAndStatus() {
        assertEquals(
            "로그인이 필요한 서비스입니다. (HTTP 401)",
            categoryErrorMessage(401, """{"success":false,"error":{"code":"UNAUTHORIZED","message":"로그인이 필요한 서비스입니다."}}"""),
        )
    }

    // 오류 JSON 누락 시 기본 문구 검증
    @Test
    fun emptyAndMalformedResponsesStillDistinguishHttpFailures() {
        assertEquals("로그인이 필요합니다. 다시 로그인해 주세요. (HTTP 401)", categoryErrorMessage(401, null))
        assertEquals("카테고리 조회 API를 찾을 수 없습니다. (HTTP 404)", categoryErrorMessage(404, "<html>error</html>"))
        assertEquals("카테고리를 불러오지 못했습니다. (HTTP 500)", categoryErrorMessage(500, ""))
    }

    // 연결·응답 해석 오류 구분 검증
    @Test
    fun transportAndParsingFailuresHaveDifferentMessages() {
        assertEquals("서버 보안 연결을 확인할 수 없습니다.", categoryFailureMessage(SSLHandshakeException("certificate")))
        assertEquals("카테고리 응답 형식이 명세와 다릅니다.", categoryFailureMessage(JsonSyntaxException("expected array")))
        assertEquals("서버에 연결하지 못했습니다. 네트워크 연결을 확인해 주세요.", categoryFailureMessage(IOException("connection")))
    }
}
