package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme

@Composable
fun SignUpScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var studentNumber by remember { mutableStateOf("") } // 학번
    var name by remember { mutableStateOf("") } // 이름
    var isWrong by remember { mutableStateOf(false) } // 틀렸는가?
    Box {
        // 뒤로가기 버튼
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

        // 상단 페이지 표시 케밥..?
        Image(
            painter = painterResource(id = R.drawable.frame_67),
            contentDescription = "디자인 미리보기",
            modifier =
                Modifier
                    .width(150.dp)
                    .padding(start = 8.dp, top = 52.dp),
            contentScale = ContentScale.Fit,
        )

        Column {
            // 제목 문구
            Text(
                text = "학번과 이름부터 알려주세요!",
                fontSize = 23.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = pretendardVariable,
                color = Color.Black,
                modifier = Modifier.padding(start = 22.dp, top = 128.dp, bottom = 23.dp),
            )

            Column(
                modifier =
                    modifier
                        .fillMaxSize()
                        .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 학번 입력창
                Column(modifier = modifier) {
                    Text(
                        fontSize = 12.sp,
                        text = "학번",
                        color = Color(0xFF999999),
                    )
                    OutlinedTextField(
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedPlaceholderColor = Color.Transparent,
                                unfocusedPlaceholderColor = Color(0xFF999999),
                                errorBorderColor = Color.Red,
                            ),
                        placeholder = { Text(text = "학번") },
                        value = studentNumber,
                        onValueChange = { studentNumber = it },
                        modifier = Modifier.padding(top = 5.dp).fillMaxWidth(0.97f),
                        shape = RoundedCornerShape(12.dp),
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        // 이름 입력창
                        Text(
                            fontSize = 12.sp,
                            text = "이름",
                            color = Color(0xFF999999),
                        )
                        OutlinedTextField(
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedPlaceholderColor = Color.Transparent,
                                    unfocusedPlaceholderColor = Color(0xFF999999),
                                    errorBorderColor = Color.Red,
                                ),
                            placeholder = { Text(text = "이름") },
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.padding(top = 5.dp).fillMaxWidth(0.97f),
                            shape = RoundedCornerShape(12.dp),
                        )
                        // 틀리면 실행되는 로직
                    }
                    if (isWrong) {
                        Text(
                            fontSize = 12.sp,
                            text = "학번과 이름을 정확히 입력해주세요.",
                            color = Color(0xFF999999),
                            modifier = Modifier.padding(top = 3.dp),
                        )
                    }
                }
            }
        }
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier =
                    Modifier
                        .padding(bottom = 10.dp),
            ) {
                Text(
                    fontSize = 14.sp,
                    text = "만약 계정이 있으신가요?",
                    color = Color(0xFFB1B1B1),
                    modifier = Modifier,
                )
                Text(
                    fontSize = 14.sp,
                    text = "로그인",
                    color = Color(0xFF3469F9),
                    textDecoration = TextDecoration.Underline,
                    modifier =
                        Modifier
                            .clickable {
                                navController.navigate(Screen.Login.route)
                            },
                )
            }
            // 다음 버튼
            Button(
                onClick = {
                    if (studentNumber.isNotBlank() && name.isNotBlank()) {
                        // 학번과 이름이 모두 입력되었을 때의 동작
                        // 백엔드 개발 이후 학번 이름을 앱에서 뭉쳐서 백엔드에 전달
                        navController.navigate(Screen.SignUp2.route)
                        isWrong = false
                    } else {
                        isWrong = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3469F9)),
                shape = RoundedCornerShape(23.dp),
                modifier =
                    Modifier
                        .fillMaxWidth(0.89f)
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

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun SignUpScreenPreview12() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.signup1),
                contentDescription = "디자인 미리보기",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .alpha(0.3f),
                contentScale = ContentScale.Fit,
            )
            // 실제 UI 겹치기
            SignUpScreen(navController = rememberNavController())
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun SignUpScreenPreview11() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            SignUpScreen(navController = rememberNavController())
        }
    }
}
