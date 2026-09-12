package com.example.deverytime_android2

import android.R.attr.clickable
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.CommonCategory
import com.example.deverytime_android2.ui.theme.CommonSearchBar
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.Style


@Composable
fun Main3Screen(navigator: NavHostController) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf("전공")
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { navigator.navigate(Screen.Main1.route) },
            modifier = Modifier
                .padding(start = 8.dp, top = 48.dp)
                .size(32.dp),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x00FFFFFF)),
        ) {
            Image(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = stringResource(R.string.back_arrow),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
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
                    println("검색 실행: $query.value")
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
                            text = category, selected = selectedCategory == category, onclick = {
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
                text = "최신순",
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

@Preview(showBackground = true, widthDp = 393, heightDp = 1150, showSystemUi = true)
@Composable
fun Main3ScreenPreview() {
    DeveryTime_Android2Theme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.main2),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.3f),
                contentScale = ContentScale.FillWidth
            )
            // 실제 UI 겹치기
            Main2Screen(rememberNavController())
        }
    }
}