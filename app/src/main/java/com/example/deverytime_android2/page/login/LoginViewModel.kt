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

sealed interface LogoutUiState {
    data object Idle : LogoutUiState
    data object Loading : LogoutUiState
    data object Success : LogoutUiState

    data class Error(
        val message: String,
    ) : LogoutUiState
}

class LoginViewModel(
    private val repository: LoginRepository = LoginRepository(),
) : ViewModel() {
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

            when (
                val result =
                    repository.login(
                        email = email,
                        password = password,
                    )
            ) {
                is LoginResult.Success -> {
                    TokenStorage.saveTokens(result.tokens)
                    _uiState.value =
                        LoginUiState.Success(result.response)
                }

                is LoginResult.Error -> {
                    _uiState.value =
                        LoginUiState.Error(result.message)
                }
            }
        }
    }

    private val _tokenReissueState =
        MutableStateFlow<TokenReissueUiState>(TokenReissueUiState.Idle)

    val tokenReissueState: StateFlow<TokenReissueUiState> =
        _tokenReissueState.asStateFlow()

    private val _logoutState =
        MutableStateFlow<LogoutUiState>(LogoutUiState.Idle)

    val logoutState: StateFlow<LogoutUiState> =
        _logoutState.asStateFlow()

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

            when (val result = repository.reissueToken(refreshToken)) {
                is TokenReissueResult.Success -> {
                    TokenStorage.saveTokens(result.tokens)
                    _tokenReissueState.value =
                        TokenReissueUiState.Success(result.tokens)
                }

                is TokenReissueResult.Error -> {
                    if (result.code == "TOKEN_INVALID") {
                        TokenStorage.clear()
                    }

                    _tokenReissueState.value =
                        TokenReissueUiState.Error(
                            code = result.code,
                            message = result.message,
                        )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    fun logout() {
        if (_logoutState.value is LogoutUiState.Loading) {
            return
        }

        val refreshToken = TokenStorage.getRefreshToken()

        if (refreshToken.isNullOrBlank()) {
            TokenStorage.clear()
            _logoutState.value = LogoutUiState.Success
            return
        }

        viewModelScope.launch {
            _logoutState.value = LogoutUiState.Loading

            when (val result = repository.logout(refreshToken)) {
                LogoutResult.Success -> {
                    TokenStorage.clear()
                    _logoutState.value = LogoutUiState.Success
                }

                is LogoutResult.Error -> {
                    _logoutState.value =
                        LogoutUiState.Error(result.message)
                }
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = LogoutUiState.Idle
    }
}
