package com.example.deverytime_android2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 상세 화면 상태
sealed interface PostDetailUiState {
    data object Loading : PostDetailUiState
    data class Success(val post: PostDetailResponse) : PostDetailUiState
    data class Error(val message: String) : PostDetailUiState
}

// 삭제 상태: 대기·요청 중·완료·실패
sealed interface DeletePostUiState {
    data object Idle : DeletePostUiState
    data object Loading : DeletePostUiState
    data object Success : DeletePostUiState
    data class Error(val message: String) : DeletePostUiState
}

class PostViewModel : ViewModel() {
    private val repository = PostRepository()
    private val _state = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val state = _state.asStateFlow()
    private var loadJob: Job? = null
    private val _deleteState = MutableStateFlow<DeletePostUiState>(DeletePostUiState.Idle)
    val deleteState = _deleteState.asStateFlow()

    // 상세 게시글 삭제·중복 요청 방지
    fun deletePost(id: Long) {
        if (_deleteState.value is DeletePostUiState.Loading ||
            _deleteState.value is DeletePostUiState.Success
        ) return
        val detail = _state.value as? PostDetailUiState.Success ?: return
        if (detail.post.id != id) return

        _deleteState.value = DeletePostUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.deletePost(id)
                // 204는 응답 본문 없이 삭제 성공
                if (response.code() == 204) {
                    _deleteState.value = DeletePostUiState.Success
                } else {
                    val message = deletePostErrorMessage(response.code(), response.errorBody()?.string())
                    _deleteState.value = DeletePostUiState.Error(message)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                _deleteState.value = DeletePostUiState.Error("네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                _deleteState.value = DeletePostUiState.Error("게시글 삭제에 실패했습니다. 다시 시도해 주세요.")
            }
        }
    }

    // 삭제 안내 닫기·상태 초기화
    fun resetDeleteState() {
        if (_deleteState.value !is DeletePostUiState.Loading) {
            _deleteState.value = DeletePostUiState.Idle
        }
    }

    // 상세 요청·화면 상태 갱신
    fun loadPost(id: Long) {
        loadJob?.cancel()
        _state.value = PostDetailUiState.Loading
        loadJob = viewModelScope.launch {
            try {
                val response = repository.getPost(id)
                val post = response.body()
                if (!response.isSuccessful) {
                    val message = postDetailErrorMessage(response.code(), response.errorBody()?.string())
                    _state.value = PostDetailUiState.Error(message)
                } else if (post == null) {
                    _state.value = PostDetailUiState.Error("게시글 상세 응답을 확인할 수 없습니다.")
                } else {
                    _state.value = PostDetailUiState.Success(post)
                }
            } catch (exception: CancellationException) {
                // 취소된 요청의 오류 처리 제외
                throw exception
            } catch (exception: IOException) {
                _state.value = PostDetailUiState.Error("네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                _state.value = PostDetailUiState.Error("게시글 상세 응답을 확인할 수 없습니다.")
            }
        }
    }
}

// 상세 오류 문구 선택 (서버 메시지 우선)
internal fun postDetailErrorMessage(httpStatus: Int, errorBody: String?): String {
    val error = try {
        Gson().fromJson(errorBody, ApiErrorResponse::class.java)?.error
    } catch (exception: Exception) {
        null
    }
    val serverMessage = error?.message
    if (!serverMessage.isNullOrBlank()) return serverMessage
    if (error?.code == "POST_NOT_FOUND" || httpStatus == 404) return "존재하지 않는 게시글입니다."
    if (httpStatus == 401) return "로그인이 필요합니다. 다시 로그인해 주세요."
    return "게시글을 불러오지 못했습니다. (HTTP $httpStatus)"
}

// 삭제 오류 문구 선택 (서버 메시지 우선)
internal fun deletePostErrorMessage(httpStatus: Int, errorBody: String?): String {
    val error = try {
        Gson().fromJson(errorBody, ApiErrorResponse::class.java)?.error
    } catch (exception: Exception) {
        null
    }
    val serverMessage = error?.message
    if (!serverMessage.isNullOrBlank()) return serverMessage
    if (error?.code == "FORBIDDEN" || httpStatus == 403) return "해당 작업을 수행할 권한이 없습니다."
    if (error?.code == "POST_NOT_FOUND" || httpStatus == 404) return "존재하지 않는 게시글입니다."
    if (httpStatus == 401) return "로그인이 필요합니다. 다시 로그인해 주세요."
    return "게시글 삭제에 실패했습니다. (HTTP $httpStatus)"
}
