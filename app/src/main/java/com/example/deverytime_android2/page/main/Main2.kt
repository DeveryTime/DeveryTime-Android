package com.example.deverytime_android2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.page.theme.CommonCategory
import com.example.deverytime_android2.page.theme.CommonSearchBar
import com.example.deverytime_android2.page.theme.Style


@Composable
fun Main2Screen(navigator: NavHostController) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf("전공")
    }
    // 목록·로딩 상태 연결
    val model: PostListViewModel = viewModel()
    val state by model.state.collectAsState()
    val listState = rememberLazyListState()
    // 카테고리 변경 시 첫 페이지 조회·스크롤 초기화
    LaunchedEffect(selectedCategory) {
        val category = temporaryPostingCategories.first { it.name == selectedCategory }
        model.select("likes", category.id.toLong())
        listState.scrollToItem(0)
    }
    // 스크롤 하단 자동 로딩
    ObservePostListEnd(listState, state, model)
    // 수정 완료 후 목록 재조회
    val entry = remember(navigator) { requireNotNull(navigator.currentBackStackEntry) }
    val postUpdated by entry.savedStateHandle.getStateFlow(POST_UPDATED_KEY, false).collectAsState()
    LaunchedEffect(postUpdated) {
        if (postUpdated) {
            model.refresh()
            entry.savedStateHandle[POST_UPDATED_KEY] = false
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { navigator.popBackStack() },
            modifier = Modifier
                .padding(start = 8.dp, top = 48.dp)
                .size(32.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = stringResource(R.string.back_arrow)
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CommonSearchBar(
                modifier = Modifier.padding(top = 20.dp),
                query = query,                          // 현재 검색어 상태 전달
                onQueryChange = { query = it },         // 입력값 변경 시 상태 업데이트
                onSearch = {                            // 검색 실행
                    navigator.currentBackStackEntry?.savedStateHandle?.set(SEARCH_QUERY_KEY, query)
                    navigator.navigate(Screen.Search.route)
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            ) {
                val categories = temporaryPostingCategories.map { it.name }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    categories.forEach { category ->
                        CommonCategory(
                            text = category, selected = selectedCategory == category, onClick = {
                                selectedCategory = it
                            })
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "인기순",
                style = Style.SubTitle,
            )
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(
                    items = state.posts, key = { _, post -> post.id }) { index, post ->
                    PostItem(
                        title = post.title,
                        time = post.createdAt,
                        like = null,
                        showTopBorder = (index == 0),
                        onClick = { navigator.navigate("postView/${post.id}") })
                }
                item { PostListStatus(state, model::retry) }
            }
        }
    }
}
