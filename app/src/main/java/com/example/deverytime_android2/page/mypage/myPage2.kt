package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.deverytime_android2.page.mypage.MyPageViewModel
import com.example.deverytime_android2.page.mypage.MyPostsUiState
import com.example.deverytime_android2.page.theme.buttonGray

val posts =
    List(24) { index ->
        Post(
            id = index,
            title =
                if (index % 3 == 0) {
                    "오늘 저녁은 치킨이다"
                } else {
                    "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다."
                },
            content = "오늘 저녁은 치킨이다",
            time = "2026-08-04T12:30:00",
            otherUserName = "다른 사용자",
            like = 5,
            comment = "마라탕",
        )
    }

@Composable
fun myPage2Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val myPageViewModel: MyPageViewModel = viewModel()

    val postsState by
    myPageViewModel.postsState.collectAsState()

    LaunchedEffect(Unit) {
        myPageViewModel.loadMyPosts()
    }

    val myPosts =
        (postsState as? MyPostsUiState.Success)
            ?.response
            ?.content
            .orEmpty()

    Box(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier =
                Modifier
                    .padding(start = 12.dp, top = 45.dp)
                    .size(32.dp),
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x00FFFFFF)),
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(id = R.string.back_arrow),
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically),
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp)
                    .align(Alignment.TopCenter),
        ) {
            Text(
                text = "내가 쓴 글",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier =
                    Modifier
                        .padding(horizontal = 17.dp),
                fontFamily = pretendardVariable,
            )
            Spacer(modifier = Modifier.height(25.dp))

            when (val state = postsState) {
                is MyPostsUiState.Loading -> {
                    Text(
                        text = "불러오는 중...",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }

                is MyPostsUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }

                is MyPostsUiState.Success -> {
                    if (myPosts.isEmpty()) {
                        Text(
                            text = "작성된 게시물이 없습니다.",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    } else {
                        LazyColumn {
                            itemsIndexed(myPosts) { index, post ->
                                PostItem(
                                    title = post.title,
                                    time = post.createdAt,
                                    trailingText = post.category,
                                    showTopBorder = index == 0,
                                )
                            }
                        }
                    }
                }

                else -> Unit
            }
        }
    }
}
