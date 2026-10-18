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
import retrofit2.Response

// 댓글 작성 상태
sealed interface CreateCommentUiState {
    data object Idle : CreateCommentUiState
    data object Loading : CreateCommentUiState
    data class Success(val comment: CommentData, val message: String) : CreateCommentUiState
    data class Error(val message: String) : CreateCommentUiState
}

// 댓글 목록 상태
sealed interface CommentListUiState {
    data object Loading : CommentListUiState
    data class Success(val comments: List<CommentListItem>) : CommentListUiState
    data class Error(val message: String) : CommentListUiState
}

class CommentViewModel : ViewModel() {
    private val repository = CommentRepository()
    private val _state = MutableStateFlow<CreateCommentUiState>(CreateCommentUiState.Idle)
    val state = _state.asStateFlow()
    private val _listState = MutableStateFlow<CommentListUiState>(CommentListUiState.Loading)
    val listState = _listState.asStateFlow()
    private var listJob: Job? = null

    // 댓글 목록 조회·이전 요청 취소
    fun loadComments(postId: Long) {
        listJob?.cancel()
        _listState.value = CommentListUiState.Loading
        listJob = viewModelScope.launch {
            try {
                val response = repository.getComments(postId)
                _listState.value = commentListResult(response)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                _listState.value = CommentListUiState.Error("네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                _listState.value = CommentListUiState.Error("댓글 목록 응답을 확인할 수 없습니다.")
            }
        }
    }

    // 빈 입력·중복 전송 방지
    fun createComment(postId: Long, content: String) {
        if (_state.value is CreateCommentUiState.Loading ||
            _state.value is CreateCommentUiState.Success
        ) return

        val trimmedContent = content.trim()
        if (trimmedContent.isEmpty()) {
            _state.value = CreateCommentUiState.Error("댓글 내용을 입력해 주세요.")
            return
        }

        _state.value = CreateCommentUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.createComment(postId, trimmedContent)
                _state.value = createCommentResult(response)
                // 등록 완료 후 댓글 목록 갱신
                if (_state.value is CreateCommentUiState.Success) {
                    loadComments(postId)
                }
            } catch (exception: CancellationException) {
                // 취소된 요청의 오류 처리 제외
                throw exception
            } catch (exception: IOException) {
                _state.value = CreateCommentUiState.Error("네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                _state.value = CreateCommentUiState.Error("댓글 작성에 실패했습니다. 다시 시도해 주세요.")
            }
        }
    }

    // 안내 완료 후 상태 초기화
    fun resetState() {
        if (_state.value !is CreateCommentUiState.Loading) {
            _state.value = CreateCommentUiState.Idle
        }
    }
}

// 빈 목록·잘못된 응답 구분
internal fun commentListResult(response: Response<CommentListResponse>): CommentListUiState {
    if (!response.isSuccessful) {
        return CommentListUiState.Error(
            commentListErrorMessage(response.code(), response.errorBody()?.string()),
        )
    }

    val body = response.body()
    val comments = body?.data
    if (response.code() != 200 || body?.success != true || comments == null) {
        return CommentListUiState.Error("댓글 목록 응답을 확인할 수 없습니다.")
    }
    return CommentListUiState.Success(comments)
}

// 목록 오류 문구 선택 (서버 메시지 우선)
internal fun commentListErrorMessage(httpStatus: Int, errorBody: String?): String {
    val error = try {
        Gson().fromJson(errorBody, ApiErrorResponse::class.java)?.error
    } catch (exception: Exception) {
        null
    }
    val serverMessage = error?.message
    if (!serverMessage.isNullOrBlank()) return serverMessage

    return when (error?.code) {
        "POST_NOT_FOUND" -> "존재하지 않는 게시글입니다."
        "INTERNAL_SERVER_ERR", "INTERNAL_SERVER_ERROR" -> "서버 측 오류가 발생했습니다."
        else -> when (httpStatus) {
            404 -> "존재하지 않는 게시글입니다."
            401 -> "로그인이 필요합니다. 다시 로그인해 주세요."
            500 -> "서버 측 오류가 발생했습니다."
            else -> "댓글 목록을 불러오지 못했습니다. (HTTP $httpStatus)"
        }
    }
}

// 상태 코드·성공 여부·댓글 응답 확인
internal fun createCommentResult(response: Response<CreateCommentResponse>): CreateCommentUiState {
    if (!response.isSuccessful) {
        val message = createCommentErrorMessage(response.code(), response.errorBody()?.string())
        return CreateCommentUiState.Error(message)
    }

    val body = response.body()
    val comment = body?.data
    if (response.code() != 201 || body?.success != true || comment == null) {
        return CreateCommentUiState.Error("댓글 작성 응답을 확인할 수 없습니다.")
    }

    val message = if (body.message.isNullOrBlank()) "댓글이 등록되었습니다." else body.message
    return CreateCommentUiState.Success(comment, message)
}

// 댓글 작성 오류 문구 선택 (서버 메시지 우선)
internal fun createCommentErrorMessage(httpStatus: Int, errorBody: String?): String {
    val error = try {
        Gson().fromJson(errorBody, ApiErrorResponse::class.java)?.error
    } catch (exception: Exception) {
        null
    }
    val serverMessage = error?.message
    if (!serverMessage.isNullOrBlank()) return serverMessage

    return when (error?.code) {
        "VALIDATION_ERROR" -> "댓글 내용을 확인해 주세요."
        "UNAUTHORIZED" -> "로그인이 필요합니다. 다시 로그인해 주세요."
        "INTERNAL_SERVER_ERR", "INTERNAL_SERVER_ERROR" -> "서버 측 오류가 발생했습니다."
        else -> when (httpStatus) {
            400 -> "댓글 내용을 확인해 주세요."
            401 -> "로그인이 필요합니다. 다시 로그인해 주세요."
            500 -> "서버 측 오류가 발생했습니다."
            else -> "댓글 작성에 실패했습니다. (HTTP $httpStatus)"
        }
    }
}
