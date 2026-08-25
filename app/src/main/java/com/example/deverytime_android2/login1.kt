package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme

@OptIn(ExperimentalTextApi::class)
val pretendardVariable =
    FontFamily(
        Font(
            resId = R.font.pretendard,
            variationSettings =
                FontVariation.Settings(
                    FontVariation.weight(FontWeight.Normal.weight),
                ),
        ),
    )
val appTypography =
    Typography(
        bodyLarge = TextStyle(fontFamily = pretendardVariable, fontWeight = FontWeight.Normal, fontSize = 16.sp),
        bodyMedium = TextStyle(fontFamily = pretendardVariable, fontWeight = FontWeight.Normal, fontSize = 14.sp),
        titleLarge = TextStyle(fontFamily = pretendardVariable, fontWeight = FontWeight.Bold, fontSize = 22.sp),
        labelLarge = TextStyle(fontFamily = pretendardVariable, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    )

@Composable
fun LoginScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Button(
        onClick = { navController.popBackStack() },
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
    Column(
        modifier =
            Modifier
                .fillMaxHeight(0.5f)
                .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Box(modifier = Modifier.padding(start = 4.dp)) {
            Text(
                text = "데브리타임을 사용하고\n일상, 전공, 멘토링 등 쉽게 소통해봐요.",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = pretendardVariable,
                color = Color.Black,
            )
        }

        Column(modifier = Modifier.padding(top = 35.dp, bottom = 48.dp)) {
            // 이메일 입력창
            Text(
                fontSize = 12.sp,
                text = "이메일",
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
                placeholder = { Text(text = "이메일") },
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(11.dp))

            // 비밀번호 입력창
            Text(
                fontSize = 12.sp,
                text = "비밀번호",
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
                placeholder = { Text(text = "비밀번호") },
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
            )
        }
    }
    Column(
        modifier =
            Modifier
                .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.padding(bottom = 10.dp)) {
            Text(
                fontSize = 14.sp,
                text = "만약 계정이 없으신가요?",
                color = Color(0xFFB1B1B1),
                modifier = Modifier,
            )
            Text(
                fontSize = 14.sp,
                text = "회원가입",
                color = Color(0xFF3469F9),
                textDecoration = TextDecoration.Underline,
                modifier =
                    Modifier
                        .clickable {
                            navController.navigate(Screen.SignUp1.route)
                        },
            )
        }
        Button(
            onClick = { },
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
                text = "로그인",
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun SignUpScreenPreview8() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.login1),
                contentDescription = "디자인 미리보기",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .alpha(0.3f),
                contentScale = ContentScale.Fit,
            )
            // 실제 UI 겹치기
            LoginScreen(navController = rememberNavController())
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun SignUpScreenPreview10() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            LoginScreen(navController = rememberNavController())
        }
    }
}
