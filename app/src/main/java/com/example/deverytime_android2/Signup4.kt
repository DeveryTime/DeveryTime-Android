package com.example.deverytime_android2

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.buttonGray
import com.example.deverytime_android2.ui.theme.mainBlue

@Composable
fun SignUp4Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // 백엔드에 있는 계정인지 true false 요청하고 true면 isIdExisting를 true로 변경
    // 추후 변경 예정

    var id by remember { mutableStateOf("") }
    var isClicked by remember { mutableStateOf(false) }
    var isIdExisting by remember { mutableStateOf(false) }
    var isWrong by remember { mutableStateOf(false) }
    val buttonColor =
        when {
            isClicked -> buttonGray

            // 버튼 클릭 후 회색
            id.isNotEmpty() -> mainBlue

            // 글자가 있으면 파란색
            else -> buttonGray // 글자가 없으면 회색
        }
    Box {
        Button(
            onClick = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier =
                Modifier
                    .padding(start = 8.dp, top = 40.dp)
                    .size(32.dp),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(50),
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

        Image(
            painter = painterResource(id = R.drawable.frame_70),
            contentDescription = "디자인 미리보기",
            modifier =
                Modifier
                    .width(150.dp)
                    .padding(start = 8.dp, top = 52.dp),
            contentScale = ContentScale.Fit,
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.weight(1.5f))
            Column {
                Text(
                    text = "사용자님의 모습이 궁금해요!",
                    fontSize = 23.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(55.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "사용자 프로필",
                        modifier =
                            Modifier
                                .width(200.dp)
                                .align(Alignment.BottomCenter),
                        contentScale = ContentScale.Fit,
                    )
                    Button(
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = { /* 이미지 업로드 로직 */ },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.frame_83),
                            contentDescription = "디자인 미리보기",
                            modifier = Modifier.padding(start = 150.dp, bottom = 2.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.CenterHorizontally),
            ) {
                // 아이디 입력창
                Text(
                    fontSize = 12.sp,
                    text = "아이디",
                    color = buttonGray,
                )
                Row {
                    OutlinedTextField(
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedPlaceholderColor = Color.Transparent,
                                unfocusedPlaceholderColor = buttonGray,
                                errorBorderColor = Color.Red,
                            ),
                        placeholder = { Text(text = "우아한 강아지") },
                        value = id,
                        onValueChange = {
                            id = it
                            isClicked = false
                        },
                        modifier = Modifier.padding(top = 3.dp).weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    Button(
                        onClick = {
                            isClicked = true
//                        if () {
//
//                        }
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = buttonColor,
                            ),
                        shape = RoundedCornerShape(12.dp),
                        modifier =
                            Modifier
                                .padding(start = 10.dp)
                                .weight(0.32f)
                                .height(56.dp)
                                .padding(top = 6.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(
                            fontFamily = pretendardVariable,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            text = "중복확인",
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                if (isIdExisting) {
                    Text(
                        fontSize = 14.sp,
                        text = "이미 있는 이름이에요.",
                        color = buttonGray,
                        modifier = Modifier.padding(top = 5.dp, start = 0.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(3.4f))
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 10.dp)) {
                Text(
                    fontSize = 14.sp,
                    text = "만약 계정이 있으신가요?",
                    color = Color(0xFFB1B1B1),
                    modifier =
                    Modifier,
                )
                Text(
                    fontSize = 14.sp,
                    text = "로그인",
                    color = mainBlue,
                    textDecoration = TextDecoration.Underline,
                    modifier =
                        Modifier
                            .clickable {
                                navController.navigate(Screen.Login.route)
                            },
                )
            }
            Box(modifier = Modifier, Alignment.BottomCenter) {
                Button(
                    onClick = {
                        // TODO: 여기서 아이디 확인 중복확인 이후 로그인으로 이동 *추가 수정 필요*
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
                    shape = RoundedCornerShape(23.dp),
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter) // Box 안에서 하단 중앙
                            .fillMaxWidth()
                            .padding(bottom = 33.dp, start = 18.dp, end = 18.dp) // 33
                            .height(54.dp),
                ) {
                    Text(
                        fontFamily = pretendardVariable,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        text = "다음",
                    )
                }
            }
        }
    }
}
