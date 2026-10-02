package com.example.deverytime_android2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = PostRepository(
        RetrofitClient.postApi
    )

    private val _searchResult = MutableStateFlow<SearchResponse?>(null)
    val searchResult: StateFlow<SearchResponse?> = _searchResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun search(keyword: String) {

        viewModelScope.launch {

            _isLoading.value = true
            _errorMessage.value = null

            try {
                val request = SearchRequest(
                    keyword = keyword
                )

                val response = repository.searchPosts(request)

                _searchResult.value = response

            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}