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

// 현재 화면에서 확인된 좋아요 상태
data class LikeUiState(
    val isLoading: Boolean = false,
    val isLiked: Boolean = false,
    val likeCount: Int? = null,
    val isReady: Boolean = false,
    val message: String? = null,
)

class LikeViewModel : ViewModel() {
    private val repository = LikeRepository()
    private val _state = MutableStateFlow(LikeUiState())
    val state = _state.asStateFlow()
    private var selectedPostId: Long? = null
    private var likeJob: Job? = null

    // 상세 응답의 좋아요 여부·개수 적용
    fun selectPost(post: PostDetailResponse) {
        if (selectedPostId == post.id) return
        likeJob?.cancel()
        selectedPostId = post.id
        _state.value = LikeUiState(
            isLiked = post.liked == true,
            likeCount = post.likeCount,
            isReady = true,
        )
    }

    // 좋아요 등록·중복 요청 방지
    fun createLike(postId: Long) {
        val currentState = _state.value
        if (selectedPostId != postId || !currentState.isReady || currentState.isLoading || currentState.isLiked) return

        _state.value = currentState.copy(isLoading = true, message = null)
        likeJob = viewModelScope.launch {
            try {
                val response = repository.createLike(postId)
                val result = createLikeResult(response, postId)
                // 실제 등록 성공 시에만 개수 증가 (409 제외)
                val count = if (response.isSuccessful && result.isLiked) {
                    currentState.likeCount?.plus(1)
                } else {
                    currentState.likeCount
                }
                _state.value = result.copy(likeCount = count, isReady = true)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                _state.value = currentState.copy(message = "네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                _state.value = currentState.copy(message = "좋아요 등록에 실패했습니다. 다시 시도해 주세요.")
            }
        }
    }

    // 좋아요 취소·중복 요청 방지
    fun deleteLike(postId: Long) {
        val currentState = _state.value
        if (selectedPostId != postId || !currentState.isReady || currentState.isLoading || !currentState.isLiked) return

        _state.value = currentState.copy(isLoading = true, message = null)
        likeJob = viewModelScope.launch {
            try {
                val response = repository.deleteLike(postId)
                val result = deleteLikeResult(response, postId)
                // 실제 취소 성공 시에만 개수 감소 (NOT_LIKED 제외)
                val count = if (response.isSuccessful && !result.isLiked) {
                    currentState.likeCount?.let { (it - 1).coerceAtLeast(0) }
                } else {
                    currentState.likeCount
                }
                _state.value = result.copy(likeCount = count, isReady = true)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                _state.value = currentState.copy(message = "네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                _state.value = currentState.copy(message = "좋아요 취소에 실패했습니다. 다시 시도해 주세요.")
            }
        }
    }

    // 좋아요 상태 유지·안내 문구만 초기화
    fun clearMessage() {
        _state.value = _state.value.copy(message = null)
    }
}

// 201·등록 여부·게시글 ID 확인
internal fun createLikeResult(response: Response<CreateLikeResponse>, postId: Long): LikeUiState {
    if (!response.isSuccessful) {
        return createLikeErrorState(response.code(), response.errorBody()?.string())
    }

    val body = response.body()
    val like = body?.data
    if (response.code() != 201 || body?.success != true || like?.liked != true || like.postId != postId) {
        return LikeUiState(message = "좋아요 등록 응답을 확인할 수 없습니다.")
    }
    val message = if (body.message.isNullOrBlank()) "좋아요가 등록되었습니다." else body.message
    return LikeUiState(isLiked = true, message = message)
}

// 서버 메시지 우선·빈 오류 응답 처리
internal fun createLikeErrorState(httpStatus: Int, errorBody: String?): LikeUiState {
    val error = try {
        Gson().fromJson(errorBody, ApiErrorResponse::class.java)?.error
    } catch (exception: Exception) {
        null
    }

    // 409는 이미 등록된 좋아요로 표시
    val isLiked = httpStatus == 409 || error?.code == "ALREADY_LIKED"
    val serverMessage = error?.message
    if (!serverMessage.isNullOrBlank()) return LikeUiState(isLiked = isLiked, message = serverMessage)

    val message = when (error?.code) {
        "ALREADY_LIKED" -> "이미 좋아요를 누른 게시글입니다."
        "POST_NOT_FOUND" -> "존재하지 않는 게시글입니다."
        "UNAUTHORIZED" -> "로그인이 필요한 서비스입니다."
        "INTERNAL_SERVER_ERR", "INTERNAL_SERVER_ERROR" -> "서버 측 오류가 발생했습니다."
        else -> when (httpStatus) {
            401 -> "로그인이 필요한 서비스입니다."
            404 -> "존재하지 않는 게시글입니다."
            409 -> "이미 좋아요를 누른 게시글입니다."
            500 -> "서버 측 오류가 발생했습니다."
            else -> "좋아요 등록에 실패했습니다. (HTTP $httpStatus)"
        }
    }
    return LikeUiState(isLiked = isLiked, message = message)
}

// 200·취소 여부·게시글 ID 확인
internal fun deleteLikeResult(response: Response<DeleteLikeResponse>, postId: Long): LikeUiState {
    if (!response.isSuccessful) {
        return deleteLikeErrorState(response.code(), response.errorBody()?.string())
    }

    val body = response.body()
    val like = body?.data
    if (response.code() != 200 || body?.success != true || like?.liked != false || like.postId != postId) {
        return LikeUiState(isLiked = true, message = "좋아요 취소 응답을 확인할 수 없습니다.")
    }
    val message = if (body.message.isNullOrBlank()) "좋아요가 취소되었습니다." else body.message
    return LikeUiState(isLiked = false, message = message)
}

// 취소 실패 시 기존 상태 유지·NOT_LIKED만 미등록으로 표시
internal fun deleteLikeErrorState(httpStatus: Int, errorBody: String?): LikeUiState {
    val error = try {
        Gson().fromJson(errorBody, ApiErrorResponse::class.java)?.error
    } catch (exception: Exception) {
        null
    }

    val isLiked = error?.code != "NOT_LIKED"
    val serverMessage = error?.message
    if (!serverMessage.isNullOrBlank()) return LikeUiState(isLiked = isLiked, message = serverMessage)

    val message = when (error?.code) {
        "NOT_LIKED" -> "좋아요를 누르지 않은 게시글입니다."
        "POST_NOT_FOUND" -> "존재하지 않는 게시글입니다."
        "UNAUTHORIZED" -> "로그인이 필요한 서비스입니다."
        "INTERNAL_SERVER_ERR", "INTERNAL_SERVER_ERROR" -> "서버 측 오류가 발생했습니다."
        else -> when (httpStatus) {
            401 -> "로그인이 필요한 서비스입니다."
            // 빈 404는 게시글 없음·좋아요 없음 구분 불가
            404 -> "게시글 또는 좋아요 정보를 확인할 수 없습니다."
            500 -> "서버 측 오류가 발생했습니다."
            else -> "좋아요 취소에 실패했습니다. (HTTP $httpStatus)"
        }
    }
    return LikeUiState(isLiked = isLiked, message = message)
}
