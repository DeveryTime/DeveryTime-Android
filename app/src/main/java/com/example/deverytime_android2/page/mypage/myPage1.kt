package com.example.deverytime_android2

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.page.theme.buttonGray
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.deverytime_android2.page.mypage.MyPageViewModel
import com.example.deverytime_android2.page.mypage.MyPageViewModelFactory
import com.example.deverytime_android2.page.mypage.MyPostsUiState
import com.example.deverytime_android2.page.mypage.MyProfileUiState

@Composable
fun MyPage1Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val myPageViewModel: MyPageViewModel =
        viewModel(
            factory =
                remember(context) {
                    MyPageViewModelFactory(
                        context.applicationContext.contentResolver,
                    )
                },
        )
    val loginViewModel: LoginViewModel = viewModel()

    val profileState by
    myPageViewModel.profileState.collectAsState()

    val logoutState by
    loginViewModel.logoutState.collectAsState()

    LaunchedEffect(Unit) {
        myPageViewModel.loadMyProfile()
        myPageViewModel.loadMyPosts(size = 3)
    }

    LaunchedEffect(logoutState) {
        if (logoutState is LogoutUiState.Error) {
            Toast.makeText(
                context,
                (logoutState as LogoutUiState.Error).message,
                Toast.LENGTH_SHORT,
            ).show()
            loginViewModel.resetLogoutState()
        }
    }

    val profile =
        (profileState as? MyProfileUiState.Success)?.profile

    val postsState by myPageViewModel.postsState.collectAsState()

    val myPosts =
        (postsState as? MyPostsUiState.Success)
            ?.response
            ?.content
            .orEmpty()

    Column(modifier = modifier.padding(top = 80.dp).fillMaxSize()) {
        Row(modifier = Modifier.padding(top = 10.dp)) {
            AsyncImage(
                model = profile?.profileImageUrl ?: R.drawable.vector_5,
                contentDescription = "마이페이지 프로필",
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .padding(start = 30.dp)
                        .size(63.dp)
                        .clip(CircleShape),
            )
            Column(
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.CenterVertically)
            ){
                Text(
                    text =
                        profile?.let {
                            "${it.name} | ${it.schoolNumber}"
                        }.orEmpty(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                )

                Text(
                    text = profile?.username.orEmpty(),
                    fontSize = 14.sp,
                    color = buttonGray,
                    fontFamily = pretendardVariable,
                )

                Text(
                    text = profile?.email.orEmpty(),
                    fontSize = 14.sp,
                    fontFamily = pretendardVariable,
                )
            }
        }
        Spacer(modifier = Modifier.height(33.dp))
        Column {
            Row(
                modifier =
                    Modifier
                        .padding(start = 22.dp)
                        .clickable {
                            navController.navigate(Screen.MyPage2.route)
                        },
            ) {
                Text(
                    text = "내가 쓴 글",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                )
                Icon(
                    modifier =
                        Modifier
                            .padding(start = 4.dp)
                            .scale(scaleX = -1f, scaleY = 1f)
                            .size(16.dp)
                            .align(Alignment.CenterVertically),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "화살표 버튼",
                )
            }
            Spacer(modifier = Modifier.height(19.dp))

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(146.4.dp),
            ) {
                when (val state = postsState) {
                    is MyPostsUiState.Loading -> {
                        Text(
                            text = "불러오는 중...",
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    is MyPostsUiState.Error -> {
                        Text(
                            text = state.message,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    is MyPostsUiState.Success -> {
                        if (myPosts.isEmpty()) {
                            Text(
                                text = "작성된 게시물이 없습니다.",
                                modifier = Modifier.align(Alignment.Center),
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                itemsIndexed(myPosts.take(3)) { index, post ->
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
        Column(
            modifier =
                Modifier
                    .padding(start = 22.dp, top = 50.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            Text(
                text = "MY",
                fontFamily = pretendardVariable,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier =
                    Modifier
                        .clickable {
                            navController.navigate(Screen.MyPage3.route)
                        },
                text = "프로필 수정",
                fontFamily = pretendardVariable,
                fontSize = 18.sp,
            )
            Text(
                text = "로그아웃",
                fontFamily = pretendardVariable,
                fontSize = 18.sp,
                modifier =
                    Modifier
                        .clickable(
                            enabled = logoutState !is LogoutUiState.Loading,
                        ) {
                            loginViewModel.logout()
                        },
            )
        }
    }
}
