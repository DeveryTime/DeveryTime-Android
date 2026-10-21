package com.example.deverytime_android2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 스크롤 위치 감지·다음 페이지 로딩
@Composable
internal fun ObservePostListEnd(listState: LazyListState, state: PostListUiState, model: PostListViewModel) {
    LaunchedEffect(listState, state.page, state.loading, state.error) {
        if (state.loading || state.error != null || !state.hasNextPage) return@LaunchedEffect
        snapshotFlow {
            val layout = listState.layoutInfo
            val lastVisibleIndex = layout.visibleItemsInfo.lastOrNull()?.index ?: -1
            val lastPostIndex = layout.totalItemsCount - 2 // 마지막 행: 로딩 상태
            layout.totalItemsCount > 0 && lastVisibleIndex >= lastPostIndex
        }.collect { nearEnd ->
            if (nearEnd) model.loadNext()
        }
    }
}

// 로딩·오류·빈 목록 표시
@Composable
internal fun PostListStatus(state: PostListUiState, retry: () -> Unit) {
    if (!state.loading && state.error == null && state.posts.isNotEmpty()) return
    Column(modifier = Modifier.padding(horizontal = 21.dp, vertical = 12.dp)) {
        when {
            state.loading -> CircularProgressIndicator()
            state.error != null -> Text(
                text = "${state.error}\n눌러서 다시 시도",
                modifier = Modifier.clickable(onClick = retry),
            )
            state.posts.isEmpty() -> Text("게시글이 없습니다.")
        }
    }
}
