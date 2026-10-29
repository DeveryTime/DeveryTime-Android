package com.example.deverytime_android2

import android.util.Log
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
    // 검색된 사용자별 작성 글 (null: 아직 못 불러왔거나 실패)
    val userPosts: Map<Long, List<PostListItem>>? = null,
    val userPostsLoading: Boolean = false,
) {
    val hasNextPage: Boolean
        get() = page + 1 < totalPages

    // 최신 게시글에서 찾은 글 + 검색된 게시글 중 해당 사용자의 글을 최신순으로 합침
    fun postsWrittenBy(userId: Long): List<PostListItem> =
        (userPosts?.get(userId).orEmpty() + posts.filter { it.writerId == userId })
            .distinctBy { it.id }
            .sortedByDescending { it.createdAt }
            .take(USER_POST_PREVIEW_COUNT)
}

private const val USER_POST_PREVIEW_COUNT = 3

class SearchViewModel(
    private val repository: SearchRepository = SearchRepository(),
) : ViewModel() {
    private val _state = MutableStateFlow(SearchUiState())
    val state = _state.asStateFlow()
    private var loadJob: Job? = null
    private var userPostsJob: Job? = null

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
                // copy로 갱신해 따로 불러오는 사용자별 작성 글 상태 유지
                _state.value = _state.value.copy(
                    users = if (page == 0) users.distinctBy { it.id } else current.users,
                    posts = (if (page == 0) posts.content else current.posts + posts.content)
                        .distinctBy { it.id },
                    loading = false,
                    error = null,
                    page = posts.page,
                    totalPages = posts.totalPages,
                )
                if (page == 0) loadUserPosts(_state.value.users.map { it.id }.toSet())
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

    // 검색된 사용자가 쓴 글 조회 (실패해도 검색 결과는 유지)
    private fun loadUserPosts(userIds: Set<Long>) {
        userPostsJob?.cancel()
        if (userIds.isEmpty()) return
        val keyword = _state.value.keyword
        _state.value = _state.value.copy(userPostsLoading = true)
        userPostsJob = viewModelScope.launch {
            val userPosts = try {
                repository.findPostsByWriters(userIds)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                if (BuildConfig.DEBUG) Log.e("SearchViewModel", "사용자 작성 글 조회 실패", exception)
                null
            }
            ensureActive()
            // 그 사이 다른 검색어로 새로 검색했으면 이전 결과는 버림
            if (_state.value.keyword != keyword) return@launch
            _state.value = _state.value.copy(userPosts = userPosts, userPostsLoading = false)
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
