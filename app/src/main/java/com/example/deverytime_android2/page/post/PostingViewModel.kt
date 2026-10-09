package com.example.deverytime_android2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParseException
import java.io.IOException
import javax.net.ssl.SSLException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 작성 상태: 대기·전송 중·성공·실패
sealed interface PostingUiState {
    data object Idle : PostingUiState
    data object Loading : PostingUiState
    data class Success(val post: CreatePostResponse) : PostingUiState
    data class Error(val message: String) : PostingUiState
}

// 카테고리 목록 상태
sealed interface PostCategoriesUiState {
    data object Loading : PostCategoriesUiState
    data class Success(val categories: List<PostCategory>) : PostCategoriesUiState
    data class Error(val message: String) : PostCategoriesUiState
}

class PostingViewModel : ViewModel() {
    private val repository = PostRepository()
    // 작성 상태 변경·화면 연결
    private val _uiState = MutableStateFlow<PostingUiState>(PostingUiState.Idle)
    val uiState = _uiState.asStateFlow()
    // 임시 카테고리 적용
    private val _categories = MutableStateFlow<PostCategoriesUiState>(
        PostCategoriesUiState.Success(temporaryPostingCategories),
    )
    val categories = _categories.asStateFlow()

    // 게시글 작성 요청
    fun createPost(categoryId: Int?, title: String, content: String) {
        val currentState = _uiState.value
        // 중복 등록 방지
        if (currentState is PostingUiState.Loading || currentState is PostingUiState.Success) return

        if (categoryId == null) {
            _uiState.value = PostingUiState.Error("카테고리를 선택해 주세요.")
            return
        }

        _uiState.value = PostingUiState.Loading
        viewModelScope.launch {
            try {
                val request = CreatePostRequest(
                    categoryId = categoryId,
                    title = title,
                    content = content,
                )
                val response = repository.createPost(request)
                val body = response.body()

                if (!response.isSuccessful) {
                    val message = postErrorMessage(response.code(), response.errorBody()?.string())
                    _uiState.value = PostingUiState.Error(message)
                } else if (body == null) {
                    _uiState.value = PostingUiState.Error("게시글 작성 응답을 확인할 수 없습니다.")
                } else {
                    _uiState.value = PostingUiState.Success(body)
                }
            } catch (exception: CancellationException) {
                // 취소된 요청의 실패 처리 제외
                throw exception
            } catch (exception: IOException) {
                _uiState.value = PostingUiState.Error("네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                _uiState.value = PostingUiState.Error("게시글 작성에 실패했습니다. 다시 시도해 주세요.")
            }
        }
    }

    // 작성 상태 초기화
    fun resetState() {
        if (_uiState.value !is PostingUiState.Loading) {
            _uiState.value = PostingUiState.Idle
        }
    }
}

// 오류 JSON 해석 (누락·형식 오류 시 null)
private fun readServerError(errorBody: String?): ApiError? {
    return try {
        val response = Gson().fromJson(errorBody, ApiErrorResponse::class.java)
        response?.error
    } catch (exception: Exception) {
        null
    }
}

// 카테고리 조회 오류 문구 선택
internal fun categoryErrorMessage(httpStatus: Int, errorBody: String?): String {
    val serverMessage = readServerError(errorBody)?.message
    val defaultMessage = when (httpStatus) {
        401 -> "로그인이 필요합니다. 다시 로그인해 주세요."
        403 -> "카테고리를 조회할 권한이 없습니다."
        404 -> "카테고리 조회 API를 찾을 수 없습니다."
        else -> "카테고리를 불러오지 못했습니다."
    }
    val message = if (!serverMessage.isNullOrBlank()) serverMessage else defaultMessage
    return "$message (HTTP $httpStatus)"
}

// 연결·응답 형식 오류 구분
internal fun categoryFailureMessage(exception: Exception): String = when (exception) {
    is SSLException -> "서버 보안 연결을 확인할 수 없습니다."
    is JsonParseException, is IllegalStateException -> "카테고리 응답 형식이 명세와 다릅니다."
    is IOException -> "서버에 연결하지 못했습니다. 네트워크 연결을 확인해 주세요."
    else -> "카테고리를 불러오지 못했습니다."
}

// 작성 오류 문구 선택 (서버 메시지 우선)
internal fun postErrorMessage(httpStatus: Int, errorBody: String?): String {
    val error = readServerError(errorBody)
    val serverMessage = error?.message

    if (!serverMessage.isNullOrBlank()) return serverMessage

    return when (error?.code) {
        "VALIDATION_ERROR" -> "필수 입력 항목입니다.에러코드"
        "USER_NOT_FOUND" -> "존재하지 않는 사용자입니다. 에러코드"
        "CATEGORY_NOT_FOUND" -> "존재하지 않는 카테고리입니다. 에러코드"
        else -> when (httpStatus) {
            401 -> "로그인이 필요합니다. 다시 로그인해 주세요."
            else -> "게시글 작성에 실패했습니다. 다시 시도해 주세요."
        }
    }
}
