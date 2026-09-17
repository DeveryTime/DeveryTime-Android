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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun Main4Screen(navigator: NavHostController) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf("전공")
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
                onSearch = {                            // 검색 버튼 누르거나 IME 액션 실행 시 동작
                    navigator.currentBackStackEntry?.savedStateHandle?.set(SEARCH_QUERY_KEY, query)
                    navigator.navigate(Screen.Search.route)
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            ) {
                val categories = listOf(
                    "전공", "일상", "교과", "급식", "프로젝트"
                )

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
                text = "조회순",
                style = Style.SubTitle,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(
                    items = dummyPosts, key = { _, post -> post.id }) { index, post ->
                    PostItem(
                        title = post.title,
                        time = post.time,
                        like = post.like,
                        showTopBorder = (index == 0),
                        onClick = {})
                }
            }
        }
    }
}
