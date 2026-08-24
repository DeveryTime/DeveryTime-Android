package com.example.deverytime_android2


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController

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
            modifier = Modifier
                .padding(start = 8.dp, top = 40.dp)
                .size(32.dp),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x00FFFFFF))
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = stringResource(id = R.string.back_arrow),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.frame_69),
            contentDescription = "디자인 미리보기",
            modifier = Modifier
                .width(150.dp)
                .padding(start = 8.dp, top = 52.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "비밀번호를 알려주세요!",
            fontSize = 23.5.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = pretendardVariable,
            color = Color.Black,
            modifier = Modifier.padding(start = 22.dp, top = 128.dp)
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(180.dp))

            // 비밀번호 입력창
            Text(
                fontSize = 12.sp,
                text = "비밀번호",
                color = Color(0xFF999999)
            )
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Transparent,
                    unfocusedPlaceholderColor = Color(0xFF999999),
                    errorBorderColor = Color.Red,
                ),
                placeholder = { Text(text = "비밀번호") },
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(9.dp))

            Text(
                fontSize = 12.sp,
                text = "비밀번호 확인",
                color = Color(0xFF999999)
            )
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color.Transparent,
                    unfocusedPlaceholderColor = Color(0xFF999999),
                    errorBorderColor = Color.Red,
                ),
                placeholder = { Text(text = "비밀번호 확인") },
                value = recheckNumber,
                onValueChange = { recheckNumber = it },
                modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation()
            )
            if (isWrong) {
                Text(
                    text = "비밀번호가 일치하지 않습니다.",
                    color = Color(0xFF999999),
                    fontSize = 12.sp,
                    modifier = modifier
                        .padding(top = 5.dp)
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                fontSize = 14.sp,
                text = "만약 계정이 있으신가요?",
                color = Color(0xFFB1B1B1),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 95.dp, end = 55.dp)
            )
            Text(
                fontSize = 14.sp,
                text = "로그인",
                color = Color(0xFF3469F9),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 95.dp, start = 135.dp)
                    .clickable {
                        navController.navigate(Screen.Login.route)
                    }
            )
            Button(
                onClick = {
                    if (password.isNotEmpty() && recheckNumber.isNotEmpty()) {
                        if (recheckNumber == password) {
                            //비밀번호와 재확인 비밀번호가 일치면 통과
                            navController.navigate(Screen.SignUp4.route)
                        } else {
                            isWrong = true
                        }
                    } else {
                        isWrong = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3469F9)),
                shape = RoundedCornerShape(23.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)      // Box 안에서 하단 중앙
                    .fillMaxWidth(0.87f)
                    .padding(bottom = 33.dp) //33
                    .height(54.dp),
            ) {
                Text(
                    fontFamily = pretendardVariable,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    text = "다음"
                )
            }
        }
    }
}