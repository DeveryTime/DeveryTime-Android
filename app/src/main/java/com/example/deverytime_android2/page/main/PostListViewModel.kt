package com.example.deverytime_android2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 목록 화면 상태
data class PostListUiState(
    val posts: List<PostListItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val page: Int = -1, // -1: 첫 조회 전
    val totalPages: Int = 0,
) {
    // 0부터 시작하는 페이지 번호
    val hasNextPage: Boolean
        get() = page + 1 < totalPages
}

class PostListViewModel : ViewModel() {
    private val repository = PostRepository()
    // 상태 변경: ViewModel / 상태 읽기: 화면
    private val _state = MutableStateFlow(PostListUiState())
    val state = _state.asStateFlow()
    private var sort = "latest"
    private var categoryId: Long? = null
    private var hasSelectedFilter = false
    private var loadJob: Job? = null

    // 정렬·카테고리 변경 시 재조회
    fun select(sort: String, categoryId: Long?) {
        val sameFilter = this.sort == sort && this.categoryId == categoryId
        if (hasSelectedFilter && sameFilter) return

        hasSelectedFilter = true
        this.sort = sort
        this.categoryId = categoryId
        refresh()
    }

    // 이전 요청 취소·목록 초기화
    fun refresh() {
        loadJob?.cancel()
        _state.value = PostListUiState()
        loadPage(0)
    }

    // 다음 페이지 조회
    fun loadNext() {
        val current = _state.value
        if (current.loading || current.error != null || !current.hasNextPage) return

        loadPage(current.page + 1)
    }

    // 실패한 페이지 재요청
    fun retry() {
        val current = _state.value
        if (current.loading) return

        val nextPage = current.page + 1
        loadPage(nextPage)
    }

    // 목록 요청·화면 상태 갱신
    private fun loadPage(page: Int) {
        _state.value = _state.value.copy(loading = true, error = null)
        // 비동기 서버 요청
        loadJob = viewModelScope.launch {
            try {
                val response = repository.getPosts(page = page, sort = sort, categoryId = categoryId)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val posts = if (page == 0) {
                        body.content
                    } else {
                        // 다음 페이지 추가·중복 ID 제거
                        (_state.value.posts + body.content).distinctBy { it.id }
                    }
                    _state.value = PostListUiState(
                        posts = posts,
                        page = body.page,
                        totalPages = body.totalPages,
                    )
                } else {
                    val message = if (response.code() == 401) {
                        "로그인이 필요합니다. 다시 로그인해 주세요."
                    } else {
                        "게시글 목록을 불러오지 못했습니다. (HTTP ${response.code()})"
                    }
                    showError(message)
                }
            } catch (exception: CancellationException) {
                // 취소된 요청의 오류 처리 제외
                throw exception
            } catch (exception: IOException) {
                showError("네트워크 연결을 확인하고 다시 시도해 주세요.")
            } catch (exception: Exception) {
                showError("게시글 목록 응답을 확인할 수 없습니다.")
            }
        }
    }

    // 기존 목록 유지·오류 표시
    private fun showError(message: String) {
        _state.value = _state.value.copy(loading = false, error = message)
    }
}
