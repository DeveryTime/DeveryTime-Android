package com.example.deverytime_android2

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.CommonCategory
import com.example.deverytime_android2.ui.theme.CommonSearchBar


import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.Style
import com.example.deverytime_android2.ui.theme.buttonGray
import java.text.SimpleDateFormat
import java.util.Locale

fun formatTime(time: String): String {
    val inputFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.KOREA,
    )

    val outputFormat = SimpleDateFormat(
        "yy.MM.dd",
        Locale.KOREA,
    )

    return runCatching {
        val date = inputFormat.parse(time)
        date?.let { outputFormat.format(it) } ?: time
    }.getOrElse {
        time
    }
}

@Composable
fun PostItem(
    title: String,
    time: String,
    like: Int,
    showTopBorder: Boolean = false,
    onClick: () -> Unit = {}
) {
    val changeTime = formatTime(time)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.8.dp)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()

                    if (showTopBorder) {
                        drawLine(
                            color = buttonGray,
                            start = Offset(0f, strokeWidth / 2),
                            end = Offset(size.width, strokeWidth / 2),
                            strokeWidth = strokeWidth,
                        )
                    }
                    drawLine(
                        color = buttonGray,
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth,
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 21.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 15.65.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = changeTime,
                    fontSize = 12.sp,
                    fontFamily = pretendardVariable,
                    color = buttonGray,
                )
            }
            Row(
                modifier = Modifier.padding(end = 21.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_thumbs_up),
                    contentDescription = stringResource(id = R.string.thumbs_up),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(18.dp),
                )
                Text(
                    text = like.toString(),
                    fontSize = 12.sp,
                    fontFamily = pretendardVariable,
                    color = buttonGray,
                )
            }
        }
    }
}

data class Post(
    val id: Int,
    val title: String,
    val time: String,
    val like: Int
)

val dummyPosts = listOf(
    Post(1, "오늘 점심 메뉴 돈까스", "2026-09-11T12:30:00", 15),
    Post(2, "안드로이드 컴포즈 스터디원 모집합니다", "2026-09-11T11:20:00", 8),
    Post(3, "프로젝트 멘토링 일정 공지 확인하세요", "2026-09-10T18:00:00", 23),
    Post(4, "프로그래밍기능사 필기 기출문제 요약본 공유", "2026-09-09T14:10:00", 42),
    Post(5, "다과실에 카드 두고 가신분 찾습니다", "2026-09-08T09:05:00", 3),
    Post(6, "취업 포트폴리오 피드백 부탁드립니다", "2026-09-07T21:40:00", 19),
    Post(7, "검은색 버즈 케이스 보신분 찾습니다", "2026-09-06T16:15:00", 5),
    Post(8, "코틀린 스터디원 모집합니다", "2026-09-05T13:25:00", 31),
)

@Composable
fun Main1Screen(navigator: NavHostController) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf("전공")
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonSearchBar(
                modifier = Modifier.padding(top = 48.dp),
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
                    "전공",
                    "일상",
                    "교과",
                    "급식",
                    "프로젝트"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    categories.forEach { category ->
                        CommonCategory(
                            text = category,
                            selected = selectedCategory == category,
                            onclick = {
                                selectedCategory = it
                            }
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                modifier = Modifier
                    .clickable{navigator.navigate(Screen.Main2.route)}
                    .padding(start = 15.dp),
                text = "인기순 >",
                style = Style.SubTitle,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(195.2.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(
                    items = dummyPosts,
                    key = { _, post -> post.id }
                ) { index, post ->
                    PostItem(
                        title = post.title,
                        time = post.time,
                        like = post.like,
                        showTopBorder = (index == 0),
                        onClick = {}
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                modifier = Modifier
                    .clickable{navigator.navigate(Screen.Main3.route)}
                    .padding(start = 15.dp),
                text = "최신순 >",
                style = Style.SubTitle,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(195.2.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(
                    items = dummyPosts,
                    key = { _, post -> post.id }
                ) { index, post ->
                    PostItem(
                        title = post.title,
                        time = post.time,
                        like = post.like,
                        showTopBorder = (index == 0),
                        onClick = {}
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                modifier = Modifier
                    .clickable{navigator.navigate(Screen.Main4.route)}
                    .padding(start = 15.dp),
                text = "조회순 >",
                style = Style.SubTitle,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(195.2.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(
                    items = dummyPosts,
                    key = { _, post -> post.id }
                ) { index, post ->
                    PostItem(
                        title = post.title,
                        time = post.time,
                        like = post.like,
                        showTopBorder = (index == 0),
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 1150, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    DeveryTime_Android2Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.main1),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.3f),
                contentScale = ContentScale.FillWidth
            )
            // 실제 UI 겹치기
            Main1Screen(rememberNavController())
        }
    }
}