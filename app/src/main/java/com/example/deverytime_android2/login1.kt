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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalTextApi::class)
val pretendardVariable = FontFamily(
    Font(
        resId = R.font.pretendard,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Normal.weight)
        )
    )
)
val appTypography = Typography(
    bodyLarge = TextStyle(fontFamily = pretendardVariable, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = pretendardVariable, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    titleLarge = TextStyle(fontFamily = pretendardVariable, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    labelLarge = TextStyle(fontFamily = pretendardVariable, fontWeight = FontWeight.Medium, fontSize = 14.sp)
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

        Text(
            text = "데브리타임을 사용하고\n일상, 전공, 멘토링 등 쉽게 소통해봐요.",
            fontSize = 23.sp,
            fontWeight = FontWeight. Bold,
            fontFamily = pretendardVariable,
            color = Color.Black,
            modifier = Modifier.padding(start = 22.dp, top = 120.dp)
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(194.dp))

            // 이메일 입력창
            Text(
                fontSize = 12.sp,
                text = "이메일",
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
                visualTransformation = PasswordVisualTransformation(),
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                fontSize = 14.sp,
                text = "만약 계정이 없으신가요?",
                color = Color(0xFFB1B1B1),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 95.dp, start = 100.dp)
            )
            Text(
                fontSize = 14.sp,
                text = "회원가입",
                color = Color(0xFF3469F9),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 95.dp, end = 105.dp)
                    .clickable {
                        navController.navigate(Screen.SignUp1.route)
                    }
            )
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3469F9)),
                shape = RoundedCornerShape(23.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)      // Box 안에서 하단 중앙
                    .width(380.dp)
                    .padding(start = 16.dp, end = 16.dp, bottom = 33.dp) //33
                    .height(54.dp),
            ) {
                Text(
                    fontFamily = pretendardVariable,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    text = "로그인"
                )
            }
        }
    }
