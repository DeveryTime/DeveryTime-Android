package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
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
fun SignUp3Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var password by remember { mutableStateOf("") }
    var recheckNumber by remember { mutableStateOf("") }
    var isWrong by remember { mutableStateOf(false) }
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
            painter = painterResource(id = R.drawable.frame_69),
            contentDescription = "디자인 미리보기",
            modifier =
                Modifier
                    .width(150.dp)
                    .padding(start = 8.dp, top = 52.dp),
            contentScale = ContentScale.Fit,
        )

        Column(modifier = Modifier) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "비밀번호를 알려주세요!",
                fontSize = 23.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = pretendardVariable,
                color = Color.Black,
                modifier = Modifier.padding(start = 22.dp),
            )

            Column(
                modifier =
                    modifier
                        .padding(16.dp),
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 비밀번호 입력창
                Text(
                    fontSize = 12.sp,
                    text = "비밀번호",
                    color = buttonGray,
                )
                OutlinedTextField(
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Transparent,
                            unfocusedPlaceholderColor = buttonGray,
                            errorBorderColor = Color.Red,
                        ),
                    placeholder = { Text(text = "비밀번호") },
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                )

                Spacer(modifier = Modifier.height(9.dp))

                Text(
                    fontSize = 12.sp,
                    text = "비밀번호 확인",
                    color = buttonGray,
                )
                OutlinedTextField(
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Transparent,
                            unfocusedPlaceholderColor = buttonGray,
                            errorBorderColor = Color.Red,
                        ),
                    placeholder = { Text(text = "비밀번호 확인") },
                    value = recheckNumber,
                    onValueChange = { recheckNumber = it },
                    modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                )
                if (isWrong) {
                    Text(
                        text = "비밀번호가 일치하지 않습니다.",
                        color = buttonGray,
                        fontSize = 12.sp,
                        modifier =
                            Modifier
                                .padding(top = 5.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(3.6f))
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
            }
            Button(
                onClick = {
                    // TODO:최소 비밀번호 8 ~ 20자까지 글자수 제한 백엔드에 검증 요청
                    if (password.isNotEmpty() && recheckNumber.isNotEmpty()) {
                        if (recheckNumber == password) {
                            // 비밀번호와 재확인 비밀번호가 일치면 통과
                            navController.navigate(Screen.SignUp4.route)
                        } else {
                            isWrong = true
                        }
                    } else {
                        isWrong = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
                shape = RoundedCornerShape(23.dp),
                modifier =
                    Modifier
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

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun SignUpScreenPreview23() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.signup4),
                contentDescription = "디자인 미리보기",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .alpha(0.3f),
                contentScale = ContentScale.Fit,
            )
            // 실제 UI 겹치기
            SignUp3Screen(navController = rememberNavController())
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun SignUpScreenPreview124() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            SignUp3Screen(navController = rememberNavController())
        }
    }
}
