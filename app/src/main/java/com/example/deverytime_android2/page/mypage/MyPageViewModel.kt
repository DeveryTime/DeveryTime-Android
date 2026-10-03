package com.example.deverytime_android2.page.mypage

import com.example.deverytime_android2.MyPostsResponse
import com.example.deverytime_android2.MyProfile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deverytime_android2.ApiErrorResponse
import com.example.deverytime_android2.MyPageRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed interface MyProfileUiState {
    data object Idle : MyProfileUiState
    data object Loading : MyProfileUiState
    data class Success(val profile: MyProfile) : MyProfileUiState
    data class Error(val message: String) : MyProfileUiState
}

sealed interface MyPostsUiState {
    data object Idle : MyPostsUiState
    data object Loading : MyPostsUiState
    data class Success(val response: MyPostsResponse) : MyPostsUiState
    data class Error(val message: String) : MyPostsUiState
}

sealed interface ProfileUpdateUiState {
    data object Idle : ProfileUpdateUiState
    data object Loading : ProfileUpdateUiState
    data class Success(val message: String) : ProfileUpdateUiState
    data class Error(val message: String) : ProfileUpdateUiState
}

sealed interface MyPageUsernameCheckUiState {
    data object Idle : MyPageUsernameCheckUiState
    data object Loading : MyPageUsernameCheckUiState

    data class Available(
        val message: String,
    ) : MyPageUsernameCheckUiState

    data class Error(
        val message: String,
    ) : MyPageUsernameCheckUiState
}

class MyPageViewModel(
    private val repository: MyPageRepository = MyPageRepository(),
) : ViewModel() {

    private val gson = Gson()

    private val _profileState =
        MutableStateFlow<MyProfileUiState>(MyProfileUiState.Idle)

    val profileState: StateFlow<MyProfileUiState> =
        _profileState.asStateFlow()

    private val _usernameCheckState =
        MutableStateFlow<MyPageUsernameCheckUiState>(
            MyPageUsernameCheckUiState.Idle,
        )

    val usernameCheckState: StateFlow<MyPageUsernameCheckUiState> =
        _usernameCheckState.asStateFlow()

    private val _postsState =
        MutableStateFlow<MyPostsUiState>(MyPostsUiState.Idle)

    val postsState: StateFlow<MyPostsUiState> =
        _postsState.asStateFlow()

    private val _updateState =
        MutableStateFlow<ProfileUpdateUiState>(
            ProfileUpdateUiState.Idle,
        )

    val updateState: StateFlow<ProfileUpdateUiState> =
        _updateState.asStateFlow()

    fun loadMyProfile() {
        if (_profileState.value is MyProfileUiState.Loading) {
            return
        }

        viewModelScope.launch {
            _profileState.value = MyProfileUiState.Loading

            try {
                val response = repository.getMyProfile()
                val body = response.body()
                val profile = body?.data

                _profileState.value =
                    if (
                        response.isSuccessful &&
                        body?.success == true &&
                        profile != null
                    ) {
                        MyProfileUiState.Success(profile)
                    } else {
                        MyProfileUiState.Error(
                            parseErrorMessage(
                                response.errorBody()?.string(),
                            ) ?: "프로필을 불러오지 못했습니다. (${response.code()})",
                        )
                    }
            } catch (exception: Exception) {
                _profileState.value =
                    MyProfileUiState.Error(
                        exception.message
                            ?: "네트워크 오류가 발생했습니다.",
                    )
            }
        }
    }

    fun loadMyPosts(
        page: Int = 0,
        size: Int = 20,
        sort: String = "",
        categoryId: Long? = null,
    ) {
        if (_postsState.value is MyPostsUiState.Loading) {
            return
        }

        viewModelScope.launch {
            _postsState.value = MyPostsUiState.Loading

            try {
                val response =
                    repository.getMyPosts(
                        page = page,
                        size = size,
                        sort = sort,
                        categoryId = categoryId,
                    )

                val body = response.body()

                _postsState.value =
                    if (response.isSuccessful && body != null) {
                        MyPostsUiState.Success(body)
                    } else {
                        MyPostsUiState.Error(
                            "${
                                parseErrorMessage(
                                    response.errorBody()?.string(),
                                ) ?: "게시글을 불러오지 못했습니다."
                            } (${response.code()})",
                        )
                    }
            } catch (exception: Exception) {
                _postsState.value =
                    MyPostsUiState.Error(
                        exception.message
                            ?: "네트워크 오류가 발생했습니다.",
                    )
            }
        }
    }
    fun checkUsername(
        username: String,
    ) {
        if (username.isBlank()) {
            _usernameCheckState.value =
                MyPageUsernameCheckUiState.Error(
                    message = "아이디를 입력해 주세요.",
                )
            return
        }

        if (_usernameCheckState.value is MyPageUsernameCheckUiState.Loading) {
            return
        }

        viewModelScope.launch {
            _usernameCheckState.value =
                MyPageUsernameCheckUiState.Loading

            try {
                val response =
                    repository.checkUsername(username)

                val body = response.body()

                _usernameCheckState.value =
                    if (
                        response.isSuccessful &&
                        body?.success == true
                    ) {
                        MyPageUsernameCheckUiState.Available(
                            message = body.message,
                        )
                    } else {
                        MyPageUsernameCheckUiState.Error(
                            message =
                                parseErrorMessage(
                                    response.errorBody()?.string(),
                                )
                                    ?: body?.message
                                    ?: "아이디 중복확인에 실패했습니다.",
                        )
                    }
            } catch (exception: Exception) {
                _usernameCheckState.value =
                    MyPageUsernameCheckUiState.Error(
                        message =
                            exception.message
                                ?: "네트워크 오류가 발생했습니다.",
                    )
            }
        }
    }

    fun resetUsernameCheckState() {
        _usernameCheckState.value =
            MyPageUsernameCheckUiState.Idle
    }
    fun updateMyProfile(
        request: RequestBody,
        profileImage: MultipartBody.Part?,
    ) {
        if (_updateState.value is ProfileUpdateUiState.Loading) {
            return
        }

        viewModelScope.launch {
            _updateState.value = ProfileUpdateUiState.Loading

            try {
                val response =
                    repository.updateMyProfile(
                        request = request,
                        profileImage = profileImage,
                    )

                val body = response.body()

                _updateState.value =
                    if (
                        response.isSuccessful &&
                        body?.success == true
                    ) {
                        ProfileUpdateUiState.Success(body.message)
                    } else {
                        ProfileUpdateUiState.Error(
                            parseErrorMessage(
                                response.errorBody()?.string(),
                            )
                                ?: body?.message
                                ?: "프로필 수정에 실패했습니다. (${response.code()})",
                        )
                    }
            } catch (exception: Exception) {
                _updateState.value =
                    ProfileUpdateUiState.Error(
                        exception.message
                            ?: "네트워크 오류가 발생했습니다.",
                    )
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = ProfileUpdateUiState.Idle
    }

    private fun parseErrorMessage(
        errorBody: String?,
    ): String? {
        if (errorBody.isNullOrBlank()) {
            return null
        }

        return runCatching {
            val error =
                gson.fromJson(
                    errorBody,
                    ApiErrorResponse::class.java,
                )?.error

            error
                ?.details
                ?.mapNotNull { it.message }
                ?.takeIf { it.isNotEmpty() }
                ?.joinToString("\n")
                ?: error?.message
        }.getOrNull()
    }
}