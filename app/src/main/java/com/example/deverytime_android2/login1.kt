package com.example.deverytime_android2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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

@OptIn(ExperimentalTextApi::class)
val PretendardVariable = FontFamily(
    Font(
        resId = R.font.pretendard,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Normal.weight)
        )
    )
)
val AppTypography = Typography(
    bodyLarge = TextStyle(fontFamily = PretendardVariable, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = PretendardVariable, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    titleLarge = TextStyle(fontFamily = PretendardVariable, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    labelLarge = TextStyle(fontFamily = PretendardVariable, fontWeight = FontWeight.Medium, fontSize = 14.sp)
)

class Login1Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeveryTime_Android2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Button(
        onClick = { /* 뒤로가기 버튼 클릭 시 동작 */ },
        modifier = Modifier
            .padding(start = 8.dp, top = 50.dp)
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

    Spacer(modifier = Modifier.height(50.dp))

    Text(
        text = "데브리타임을 사용하고\n일상, 전공, 멘토링 등 쉽게 소통해봐요.",
        fontSize = 24.sp,
        fontWeight = FontWeight. Bold,
        fontFamily = PretendardVariable,
        color = Color.Black,
        modifier = Modifier.padding(start = 16.dp, top = 100.dp)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(187.dp))

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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        )

        Spacer(modifier = Modifier.height(2.dp))

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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
        )
    }
}

// 디자인 이미지 + 실제 UI를 한 화면에 겹쳐서 보여주는 Preview
@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.login1),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Fit
            )
            // 실제 UI 겹치기
            LoginScreen()
        }
    }
}
@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun LoginScreenPreview2() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            LoginScreen()
        }
    }
}
