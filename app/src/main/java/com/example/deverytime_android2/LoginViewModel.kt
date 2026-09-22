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

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}
