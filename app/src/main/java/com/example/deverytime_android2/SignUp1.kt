package com.example.deverytime_android2

import android.os.Bundle
import android.text.style.BackgroundColorSpan
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen_Signup(val route: String) {
    data object SignUp1 : Screen_Signup("signup1")
    data object Login : Screen_Signup("login")
}

class SignUp1Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeveryTime_Android2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
    @Composable
    fun Navigation(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen_Signup.SignUp1.route,
            modifier = modifier
        ) {
            composable(route = Screen_Signup.SignUp1.route) { SignUpScreen(navController = navController) }
            composable(route = Screen_Signup.Login.route) { LoginScreen(navController = navController) }
        }
    }
}

@Composable
fun SignUpScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var student_number by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    Button(
        onClick = { navController.popBackStack() },
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

    Image(
        painter = painterResource(id = R.drawable.frame_67),
        contentDescription = "디자인 미리보기",
        modifier = Modifier
            .width(150.dp)
            .padding(start = 8.dp, top = 62.dp),
        contentScale = ContentScale.Fit
    )

    Text(
        text = "학번과 이름부터 알려주세요!",
        fontSize = 23.5.sp,
        fontWeight = FontWeight. Bold,
        fontFamily = PretendardVariable,
        color = Color.Black,
        modifier = Modifier.padding(start = 22.dp, top = 128.dp)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(180.dp))

        // 이메일 입력창
        Text(
            fontSize = 12.sp,
            text = "학번",
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
            placeholder = { Text(text = "학번") },
            value = student_number,
            onValueChange = { student_number = it },
            modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        )

        Spacer(modifier = Modifier.height(11.dp))

        // 이름 입력창
        Text(
            fontSize = 12.sp,
            text = "이름",
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
            placeholder = { Text(text = "이름") },
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.padding(top = 3.dp).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        )
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
                fontFamily = PretendardVariable,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                text = "다음"
            )
        }
    }
}

// 디자인 이미지 + 실제 UI를 한 화면에 겹쳐서 보여주는 Preview
@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.signup1),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Fit
            )
            // 실제 UI 겹치기
            SignUpScreen(navController = rememberNavController())
        }
    }
}
@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun SignUpScreenPreview2() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            SignUpScreen(navController = rememberNavController())
        }
    }
}
