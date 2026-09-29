package com.example.deverytime_android2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    data object Idle : LoginUiState

    data object Loading : LoginUiState

    data class Success(
        val response: LoginResponse,
    ) : LoginUiState

    data class Error(
        val message: String,
    ) : LoginUiState
}

sealed interface TokenReissueUiState {
    data object Idle : TokenReissueUiState
    data object Loading : TokenReissueUiState

    data class Success(
        val tokens: TokenData,
    ) : TokenReissueUiState

    data class Error(
        val code: String?,
        val message: String,
    ) : TokenReissueUiState
}

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()

    private val _uiState =
        MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun login(
        email: String,
        password: String,
    ) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value =
                LoginUiState.Error("이메일과 비밀번호를 입력해 주세요.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            try {
                val response =
                    repository.login(
                        email = email,
                        password = password,
                    )

                if (response.isSuccessful) {
                    val body = response.body()

                    _uiState.value =
                        if (body != null) {
                            TokenStorage.saveTokens(body.data)
                            LoginUiState.Success(body)
                        } else {
                            LoginUiState.Error("응답 데이터가 없습니다.")
                        }
                } else {
                    _uiState.value =
                        LoginUiState.Error(
                            "로그인 실패: ${response.code()}",
                        )
                }
            } catch (exception: Exception) {
                _uiState.value =
                    LoginUiState.Error(
                        exception.message
                            ?: "네트워크 오류가 발생했습니다.",
                    )
            }
        }
    }

    private val _tokenReissueState =
        MutableStateFlow<TokenReissueUiState>(TokenReissueUiState.Idle)

    val tokenReissueState: StateFlow<TokenReissueUiState> =
        _tokenReissueState.asStateFlow()

    fun reissueToken(refreshToken: String) {
        if (refreshToken.isBlank()) {
            _tokenReissueState.value =
                TokenReissueUiState.Error(
                    code = "TOKEN_INVALID",
                    message = "리프레시 토큰이 없습니다.",
                )
            return
        }

        viewModelScope.launch {
            _tokenReissueState.value = TokenReissueUiState.Loading

            try {
                val response = repository.reissueToken(refreshToken)
                val body = response.body()
                val tokens = body?.data

                _tokenReissueState.value =
                    when {
                        response.isSuccessful && body?.success == true && tokens != null -> {
                            TokenStorage.saveTokens(tokens)
                            TokenReissueUiState.Success(tokens)
                        }

                        response.code() == 401 -> {
                            TokenStorage.clear()

                            TokenReissueUiState.Error(
                                code = "TOKEN_INVALID",
                                message = "유효하지 않은 토큰입니다.",
                            )
                        }
                        else ->
                            TokenReissueUiState.Error(
                                code = null,
                                message = "토큰 재발급에 실패했습니다. (${response.code()})",
                            )
                    }
            } catch (exception: Exception) {
                _tokenReissueState.value =
                    TokenReissueUiState.Error(
                        code = null,
                        message = exception.message ?: "네트워크 오류가 발생했습니다.",
                    )
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}
