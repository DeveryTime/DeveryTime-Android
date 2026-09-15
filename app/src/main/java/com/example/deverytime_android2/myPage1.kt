package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.deverytime_android2.ui.theme.buttonGray

var name = "박XX"
var userName = "발랄한 바둑이"
var schoolNumber = 1107
var userEmail = "deverytime2026@gmail.com"

@Composable
fun myPage1Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(top = 80.dp).fillMaxSize()) {
        Row(modifier = Modifier.padding(top = 10.dp)) {
            // TODO: 사진을 백엔드에서 가져와야함
            Image(
                painter = painterResource(id = R.drawable.vector_5),
                contentDescription = "마이페이지 프로필",
                modifier =
                    Modifier
                        .padding(start = 30.dp),
            )
            Column(modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)) {
                Text(
                    text = "$name | $schoolNumber",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                )
                Text(
                    text = userName,
                    fontSize = 14.sp,
                    color = buttonGray,
                    fontFamily = pretendardVariable,
                )
                Text(
                    text = userEmail,
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
            LazyColumn(
                modifier =
                    Modifier
                        .padding(top = 19.dp),
            ) {
                items(3) { index ->
                    PostItem(
                        post = posts[index],
                        navController = navController,
                        showTopBorder = index == 0,
                    )
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
                        .clickable {
                            // TODO: 토큰 삭제 로직 추가 (백엔드)
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.MyPage1.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
            )
        }
    }
}
