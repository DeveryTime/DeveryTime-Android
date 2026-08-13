package com.example.deverytime_android2

import android.os.Bundle
import android.text.style.BackgroundColorSpan
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import java.security.cert.Certificate



@Composable
fun SignUp2Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") } //이메일
    var CertifiedNum by remember { mutableStateOf("") } //인증번호 추후에 받아야함
    var Certified by remember { mutableStateOf(false) } //인증버튼이 눌렸는지 안 눌렸는지
    var OnCertified by remember { mutableStateOf(false) } //인증번호가 맞는지 틀린지
    var isVisible by remember { mutableStateOf(true) } //보이는지 안보이는지
    var isClicked by remember { mutableStateOf(false) } //재전송 버튼 색상 변경 변수
    val scope = rememberCoroutineScope() //5초 카운트 변수
    var elapsedSecond by remember { mutableStateOf(0) } //카운트 업 변수
    var iswrong by remember { mutableStateOf(false) } //인증번호가 틀렸을 때 true로 바뀌는 변수


    //경과시간 계산 포맷
    val minutes = elapsedSecond / 60 //분
    val seconds = elapsedSecond % 60 //초
    val formattedTime = "%02d:%02d".format(minutes, seconds)

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

    Image(
        painter = painterResource(id = R.drawable.frame_68),
        contentDescription = "디자인 미리보기",
        modifier = Modifier
            .width(150.dp)
            .padding(start = 8.dp, top = 52.dp),
        contentScale = ContentScale.Fit
    )

    Text(
        text = "이메일을 인증해주세요!",
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
            modifier = Modifier
                .padding(top = 3.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        )
            Spacer(modifier = Modifier.height(9.dp))
        if (Certified) { // 여기 수정해라 조껀히 년아
            Column {
                // 인증번호 입력창
                Text(
                    fontSize = 12.sp,
                    text = "인증번호",
                    color = Color(0xFF999999)
                )
                Row (modifier = Modifier.height(62.dp)) {
                    OutlinedTextField(
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Transparent,
                            unfocusedPlaceholderColor = Color(0xFF999999),
                            errorBorderColor = Color.Red,
                        ),
                        placeholder = { Text(text = "인증번호") },
                        trailingIcon = {
                            Text(
                                modifier = modifier.padding(end = 12.dp),
                                text = "%02d:%02d".format(
                                    elapsedSecond / 60,
                                    elapsedSecond % 60
                                ),
                                fontSize = 16.sp,
                                color = Color(0xFF999999),
                            )
                        },
                        value = CertifiedNum,
                        onValueChange = { CertifiedNum = it },
                        modifier = Modifier
                            .padding(top = 3.dp)
                            .fillMaxWidth(0.78f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    LaunchedEffect(Certified) {
                        if (Certified) {
                            elapsedSecond = 0

                            while (true) {
                                delay(1000L)
                                elapsedSecond++
                            }
                        }
                    }
                    LaunchedEffect(Unit) {
                        delay(5000L) // 5초 대기
                        isClicked = true
                    }
                    Button(
                        onClick = {
                            elapsedSecond = 0
                            if (isClicked) {
                                isClicked = false

                                scope.launch {
                                    delay(5000L)
                                    isClicked = true
                                }
                            }
                        },
                        enabled = isClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isClicked) {
                                Color(0xFF3469F9)
                            } else {
                                Color(0xFF999999)
                            }
                        ),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.padding(start = 10.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .align(Alignment.CenterVertically),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(
                            fontFamily = PretendardVariable,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            text = "재전송",
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (iswrong) {
                    Text(
                        fontSize = 12.sp,
                        text = "인증번호가 달라요.",
                        color = Color(0xFF999999),
                        modifier = Modifier.padding(top = 5.dp)
                    )
                } //틀렸을 떄 나오는 문구
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            fontSize = 14.sp,
            text = "만약 계정이 있으신가요?",
            color = Color(0xFFB1B1B1),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 95.dp, start = 105.dp)
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
        if (isVisible) {
            Button(
                onClick = {
                    Certified = true
                    isVisible = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3469F9)),
                shape = RoundedCornerShape(23.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(380.dp)
                    .padding(start = 16.dp, end = 16.dp, bottom = 33.dp) //33
                    .height(54.dp)
                    .zIndex(1f)
            ) {
                Text(
                    fontFamily = PretendardVariable,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    text = "이메일 인증"
                )
            }
        }
        Button(
            onClick = {
                //임시
                navController.navigate(Screen.SignUp3.route)
                //이 이후로 판별코드
                if (OnCertified == true) {

                } //여기에 만약 인증이 되면 뭐할건지 로직 추가
                else {

                } //여기에 인증 안된채로 누르면 어떤 반응 할지
            },
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
@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun SignUp2ScreenPreview() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.signup2),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Fit
            )
            // 실제 UI 겹치기
            SignUp2Screen(navController = rememberNavController())
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun SignUp2ScreenPreview2() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            SignUp2Screen(navController = rememberNavController())
        }
    }
}