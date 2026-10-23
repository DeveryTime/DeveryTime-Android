package com.example.deverytime_android2.page.mypage

import android.util.Log
import com.example.deverytime_android2.BuildConfig
import com.example.deverytime_android2.MyPostSummary
import com.example.deverytime_android2.MyPostsApiResponse
import com.example.deverytime_android2.MyPostsResponse
import com.example.deverytime_android2.MyProfile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deverytime_android2.ApiErrorResponse
import com.example.deverytime_android2.MyPageRepository
import com.google.gson.Gson
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

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

    data class Loading(
        val checkedUsername: String,
    ) : MyPageUsernameCheckUiState

    data class Available(
        val checkedUsername: String,
        val message: String,
    ) : MyPageUsernameCheckUiState

    data class Error(
        val checkedUsername: String,
        val message: String,
    ) : MyPageUsernameCheckUiState
}

class MyPageViewModel(
    private val repository: MyPageRepository,
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

    private var usernameCheckRequestId = 0L

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
        sort: String = "createdAt,desc",
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

                _postsState.value = myPostsResult(response)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                logMyPostsFailure(exception)
                _postsState.value = MyPostsUiState.Error("네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                logMyPostsFailure(exception)
                _postsState.value = MyPostsUiState.Error("내가 쓴 글 응답을 확인할 수 없습니다.")
            }
        }
    }

    private fun logMyPostsFailure(exception: Exception) {
        if (BuildConfig.DEBUG) {
            Log.e("MyPageViewModel", "내가 쓴 글 조회 실패", exception)
        }
    }
    fun checkUsername(
        username: String,
    ) {
        if (username.isBlank()) {
            usernameCheckRequestId++
            _usernameCheckState.value =
                MyPageUsernameCheckUiState.Error(
                    checkedUsername = username,
                    message = "아이디를 입력해 주세요.",
                )
            return
        }

        val currentState = _usernameCheckState.value
        if (
            currentState is MyPageUsernameCheckUiState.Loading &&
            currentState.checkedUsername == username
        ) {
            return
        }

        val requestId = ++usernameCheckRequestId

        viewModelScope.launch {
            _usernameCheckState.value =
                MyPageUsernameCheckUiState.Loading(
                    checkedUsername = username,
                )

            try {
                val response =
                    repository.checkUsername(username)

                val body = response.body()

                if (requestId != usernameCheckRequestId) {
                    return@launch
                }

                _usernameCheckState.value =
                    if (
                        response.isSuccessful &&
                        body?.success == true
                    ) {
                        MyPageUsernameCheckUiState.Available(
                            checkedUsername = username,
                            message = body.message,
                        )
                    } else {
                        MyPageUsernameCheckUiState.Error(
                            checkedUsername = username,
                            message =
                                parseErrorMessage(
                                    response.errorBody()?.string(),
                                )
                                    ?: body?.message
                                    ?: "아이디 중복확인에 실패했습니다.",
                        )
                    }
            } catch (exception: Exception) {
                if (requestId != usernameCheckRequestId) {
                    return@launch
                }

                _usernameCheckState.value =
                    MyPageUsernameCheckUiState.Error(
                        checkedUsername = username,
                        message =
                            exception.message
                                ?: "네트워크 오류가 발생했습니다.",
                    )
            }
        }
    }

    fun resetUsernameCheckState() {
        usernameCheckRequestId++
        _usernameCheckState.value =
            MyPageUsernameCheckUiState.Idle
    }
    fun updateMyProfile(
        username: String,
        deleteProfileImage: Boolean,
        profileImageUri: String?,
    ) {
        if (_updateState.value is ProfileUpdateUiState.Loading) {
            return
        }

        viewModelScope.launch {
            _updateState.value = ProfileUpdateUiState.Loading

            try {
                val response =
                    repository.updateMyProfile(
                        username = username,
                        deleteProfileImage = deleteProfileImage,
                        profileImageUri = profileImageUri,
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

// content: []는 정상 빈 목록이며, content 누락은 응답 오류로 구분
internal fun myPostsResult(response: Response<MyPostsApiResponse>): MyPostsUiState {
    if (!response.isSuccessful) {
        val error = runCatching {
            Gson().fromJson(response.errorBody()?.string(), ApiErrorResponse::class.java)?.error
        }.getOrNull()
        val message = error?.message?.takeIf { it.isNotBlank() }
            ?: if (response.code() == 401) {
                "로그인이 필요합니다. 다시 로그인해 주세요."
            } else {
                "내가 쓴 글을 불러오지 못했습니다. 다시 시도해 주세요."
            }
        return MyPostsUiState.Error(message)
    }

    val result = response.body()
    val body = result?.data
    val posts: List<MyPostSummary>? = body?.content
    if (result?.success != true || body == null || posts == null || posts.any {
            listOf<String?>(it.title, it.category, it.createdAt).any { field -> field == null }
        }) {
        return MyPostsUiState.Error("내가 쓴 글 응답을 확인할 수 없습니다.")
    }
    return MyPostsUiState.Success(body)
}
