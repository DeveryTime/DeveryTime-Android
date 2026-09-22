package com.example.deverytime_android2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SignUpFormState(
    val schoolNumber: String = "",
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
)

class SignUpViewModel : ViewModel() {

    private val _formState = MutableStateFlow(SignUpFormState())

    val formState: StateFlow<SignUpFormState> =
        _formState.asStateFlow()
}