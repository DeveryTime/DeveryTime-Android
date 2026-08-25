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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

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
            isClicked -> Color(0xFF999999)

            // 버튼 클릭 후 회색
            id.isNotEmpty() -> Color(0xFF3469F9)

            // 글자가 있으면 파란색
            else -> Color(0xFF999999) // 글자가 없으면 회색
        }
    Box(modifier = modifier.fillMaxSize()) {
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
        Column {
            Spacer(modifier = Modifier.height(128.dp))
            Text(
                text = "사용자님의 모습이 궁금해요!",
                fontSize = 23.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = pretendardVariable,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(50.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = "디자인 미리보기",
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
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = 183.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            // 아이디 입력창
            Text(
                fontSize = 12.sp,
                text = "아이디",
                color = Color(0xFF999999),
            )
            Row {
                OutlinedTextField(
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Transparent,
                            unfocusedPlaceholderColor = Color(0xFF999999),
                            errorBorderColor = Color.Red,
                        ),
                    placeholder = { Text(text = "우아한 강아지") },
                    value = id,
                    onValueChange = {
                        id = it
                        isClicked = false
                    },
                    modifier = Modifier.padding(top = 3.dp).weight(0.5f),
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
                            .weight(0.15f)
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
                    color = Color(0xFF999999),
                    modifier = Modifier.padding(top = 5.dp, start = 0.dp),
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                fontSize = 14.sp,
                text = "만약 계정이 있으신가요?",
                color = Color(0xFFB1B1B1),
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 95.dp, end = 55.dp),
            )
            Text(
                fontSize = 14.sp,
                text = "로그인",
                color = Color(0xFF3469F9),
                textDecoration = TextDecoration.Underline,
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 95.dp, start = 135.dp)
                        .clickable {
                            navController.navigate(Screen.Login.route)
                        },
            )
            Button(
                onClick = {
                    // TODO: 여기서 아이디 확인 중복확인 이후 로그인으로 이동 *추가 수정 필요*
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3469F9)),
                shape = RoundedCornerShape(23.dp),
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter) // Box 안에서 하단 중앙
                        .fillMaxWidth(0.87f)
                        .padding(bottom = 33.dp) // 33
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
