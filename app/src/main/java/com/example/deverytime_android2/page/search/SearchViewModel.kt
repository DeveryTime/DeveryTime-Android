package com.example.deverytime_android2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val keyword: String = "",
    val users: List<SearchUserResult> = emptyList(),
    val posts: List<PostListItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false,
    val page: Int = -1,
    val totalPages: Int = 0,
) {
    val hasNextPage: Boolean
        get() = page + 1 < totalPages
}

class SearchViewModel(
    private val repository: SearchRepository = SearchRepository(),
) : ViewModel() {
    private val _state = MutableStateFlow(SearchUiState())
    val state = _state.asStateFlow()
    private var loadJob: Job? = null

    fun search(query: String) {
        loadJob?.cancel()
        val keyword = query.trim()
        if (keyword.isEmpty()) {
            _state.value = SearchUiState(error = "검색어를 입력해 주세요.")
            return
        }
        _state.value = SearchUiState(keyword = keyword, hasSearched = true)
        loadPage(0)
    }

    fun loadNext() {
        val current = _state.value
        if (current.loading || current.error != null || !current.hasNextPage) return
        loadPage(current.page + 1)
    }

    fun retry() {
        val current = _state.value
        if (current.loading || !current.hasSearched) return
        loadPage(current.page + 1)
    }

    private fun loadPage(page: Int) {
        val current = _state.value
        _state.value = current.copy(loading = true, error = null)
        loadJob = viewModelScope.launch {
            try {
                val response = repository.search(keyword = current.keyword, page = page)
                // 새 검색이 시작되어 취소된 요청은 결과를 덮어쓰지 않음
                ensureActive()
                if (!response.isSuccessful) {
                    showError(searchErrorMessage(response.code(), response.errorBody()?.string()))
                    return@launch
                }
                val result = response.body()
                if (result?.success != true) {
                    showError(result?.error?.message?.takeIf { it.isNotBlank() }
                        ?: "검색 응답을 확인할 수 없습니다.")
                    return@launch
                }
                val data = result.data
                val users = data?.users
                val posts = data?.posts
                if (users == null || posts == null) {
                    showError("검색 응답을 확인할 수 없습니다.")
                    return@launch
                }
                _state.value = SearchUiState(
                    keyword = current.keyword,
                    users = if (page == 0) users.distinctBy { it.id } else current.users,
                    posts = (if (page == 0) posts.content else current.posts + posts.content)
                        .distinctBy { it.id },
                    hasSearched = true,
                    page = posts.page,
                    totalPages = posts.totalPages,
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: IOException) {
                ensureActive()
                showError("네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                ensureActive()
                showError("검색 응답을 확인할 수 없습니다.")
            }
        }
    }

    private fun showError(message: String) {
        _state.value = _state.value.copy(loading = false, error = message)
    }
}

internal fun searchErrorMessage(httpStatus: Int, errorBody: String?): String {
    val error = try {
        Gson().fromJson(errorBody, ApiErrorResponse::class.java)?.error
    } catch (exception: Exception) {
        null
    }
    error?.message?.takeIf { it.isNotBlank() }?.let { return it }
    return when {
        httpStatus == 401 || error?.code == "UNAUTHORIZED" -> "로그인이 필요한 서비스입니다."
        httpStatus == 400 || error?.code == "VALIDATION_ERROR" -> "입력값이 올바르지 않습니다."
        else -> "검색에 실패했습니다. (HTTP $httpStatus)"
    }
}
