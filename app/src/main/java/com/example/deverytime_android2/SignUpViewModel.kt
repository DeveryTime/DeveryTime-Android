package com.example.deverytime_android2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpFormState(
    val schoolNumber: String = "",
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
)

sealed interface SignUpUiState {
    data object Idle : SignUpUiState

    data object Loading : SignUpUiState

    data class Success(
        val message: String,
    ) : SignUpUiState

    data class Error(
        val errorCode: String?,
        val message: String,
    ) : SignUpUiState
}

class SignUpViewModel(
    private val repository: SignUpRepository = SignUpRepository(),
) : ViewModel() {

    private val _formState = MutableStateFlow(SignUpFormState())

    val formState: StateFlow<SignUpFormState> =
        _formState.asStateFlow()

    private val _uiState =
        MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)

    val uiState: StateFlow<SignUpUiState> =
        _uiState.asStateFlow()

    fun updateSchoolInfo(
        schoolNumber: String,
        name: String,
    ) {
        _formState.update { currentState ->
            currentState.copy(
                schoolNumber = schoolNumber,
                name = name,
            )
        }
    }

    fun updateEmail(email: String) {
        _formState.update { currentState ->
            currentState.copy(email = email)
        }
    }

    fun updatePassword(
        password: String,
        passwordConfirm: String,
    ) {
        _formState.update { currentState ->
            currentState.copy(
                password = password,
                passwordConfirm = passwordConfirm,
            )
        }
    }

    fun updateUsername(username: String) {
        _formState.update { currentState ->
            currentState.copy(username = username)
        }
    }

    fun signUp() {
        if (_uiState.value is SignUpUiState.Loading) {
            return
        }

        val request = createRequest()

        if (
            request.schoolNumber.isBlank() ||
            request.name.isBlank() ||
            request.email.isBlank() ||
            request.username.isBlank() ||
            request.password.isBlank() ||
            request.passwordConfirm.isBlank()
        ) {
            _uiState.value =
                SignUpUiState.Error(
                    errorCode = "VALIDATION_ERROR",
                    message = "입력하지 않은 정보가 있습니다.",
                )
            return
        }

        if (request.password != request.passwordConfirm) {
            _uiState.value =
                SignUpUiState.Error(
                    errorCode = "VALIDATION_ERROR",
                    message = "비밀번호가 일치하지 않습니다.",
                )
            return
        }

        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading

            try {
                val response = repository.signUp(request)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null && body.success) {
                        _uiState.value =
                            SignUpUiState.Success(body.message)
                    } else {
                        _uiState.value =
                            SignUpUiState.Error(
                                errorCode = null,
                                message = body?.message
                                    ?: "회원가입 응답 데이터가 없습니다.",
                            )
                    }
                } else {
                    val errorBody =
                        response.errorBody()?.string().orEmpty()

                    val errorCode = findErrorCode(errorBody)

                    _uiState.value =
                        SignUpUiState.Error(
                            errorCode = errorCode,
                            message = errorMessage(
                                errorCode = errorCode,
                                httpCode = response.code(),
                            ),
                        )
                }
            } catch (exception: Exception) {
                _uiState.value =
                    SignUpUiState.Error(
                        errorCode = null,
                        message = exception.message
                            ?: "네트워크 오류가 발생했습니다.",
                    )
            }
        }
    }

    private fun createRequest(): SignUpRequest {
        val currentState = _formState.value

        return SignUpRequest(
            schoolNumber = currentState.schoolNumber,
            name = currentState.name,
            email = currentState.email,
            username = currentState.username,
            password = currentState.password,
            passwordConfirm = currentState.passwordConfirm,
        )
    }

    private fun findErrorCode(errorBody: String): String? {
        val knownErrorCodes =
            listOf(
                "VALIDATION_ERROR",
                "EMAIL_ALREADY_EXISTS",
                "USERNAME_ALREADY_EXISTS",
                "EMAIL_NOT_VERIFIED",
            )

        return knownErrorCodes.firstOrNull { errorCode ->
            errorBody.contains(errorCode)
        }
    }

    private fun errorMessage(
        errorCode: String?,
        httpCode: Int,
    ): String {
        return when (errorCode) {
            "VALIDATION_ERROR" ->
                "입력한 정보를 다시 확인해 주세요."

            "EMAIL_ALREADY_EXISTS" ->
                "이미 가입된 이메일입니다."

            "USERNAME_ALREADY_EXISTS" ->
                "이미 사용 중인 아이디입니다."

            "EMAIL_NOT_VERIFIED" ->
                "이메일 인증을 먼저 완료해 주세요."

            else ->
                "회원가입에 실패했습니다. ($httpCode)"
        }
    }

    fun resetUiState() {
        _uiState.value = SignUpUiState.Idle
    }

    fun clearForm() {
        _formState.value = SignUpFormState()
    }
}