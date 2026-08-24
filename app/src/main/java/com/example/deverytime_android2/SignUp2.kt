package com.example.deverytime_android2


import android.util.Patterns
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.regex.Pattern

private const val SCHOOL_EMAIL_DOMAIN = "dsm.hs.kr"

@Composable
    fun SignUp2Screen(
        navController: NavHostController,
        modifier: Modifier = Modifier,
    ) {
        var email by remember { mutableStateOf("") } //이메일
        var certifiedNum by remember { mutableStateOf("") } //사용자가 입력한 인증번호
        var receiveRealCertifiedNum by remember { mutableStateOf("1234") } //서버에서 발급해준 진짜 인증번호
        var onClickCertified by remember { mutableStateOf(false) } //인증버튼이 눌렸는지 안 눌렸는지
        var certifiedIsRealOrNot by remember { mutableStateOf(false) } //인증번호가 맞는지 틀린지 (추후 백엔드 연동 예정)
        var isVisible by remember { mutableStateOf(true) } //보이는지 안보이는지
        var isClicked by remember { mutableStateOf(false) } //재전송 버튼 색상 변경 변수
        val scope = rememberCoroutineScope() //5초 카운트 변수
        var elapsedSecond by remember { mutableStateOf(0) } //카운트 업 변수
        var isWrong by remember { mutableStateOf(false) } //인증번호가 틀렸을 때 true로 바뀌는 변수
        var isEmailWrong by remember { mutableStateOf(false) } //이메일 형식이 틀렸을 때 true로 바뀌는 변수
        var timeDone by remember { mutableStateOf(false) } //시간이 다 지났는지 확인하는 변수

        //경과시간 계산 포맷
        val minutes = elapsedSecond / 60 //분
        val seconds = elapsedSecond % 60 //초
        val formattedTime = "%02d:%02d".format(minutes, seconds)
        Box {
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
                    ),
                    placeholder = { Text(text = "이메일") },
                    value = email,
                    onValueChange = { input ->
                        email = input
                            .substringBefore("@")
                            .filter { it.isDigit() }
                            isEmailWrong = false
                            timeDone = false
                            isVisible = true
                            onClickCertified = false
                            elapsedSecond = 0
                            certifiedNum = ""
                        },
                    suffix = {
                      Text("@$SCHOOL_EMAIL_DOMAIN")
                    },
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = isEmailWrong,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                if (isEmailWrong) {
                    Text(
                        text = "잘못된 이메일 형식입니다.",
                        fontSize = 12.sp,
                        color = Color(0xFF999999),
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(9.dp))
                if (onClickCertified == true && email.isNotEmpty()) { // 여기 수정해라 조껀히 년아
                    Column {
                        // 인증번호 입력창
                        Text(
                            fontSize = 12.sp,
                            text = "인증번호",
                            color = Color(0xFF999999)
                        )
                        Row(modifier = Modifier.height(62.dp)) {
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
                                value = certifiedNum,
                                onValueChange = { certifiedNum = it },
                                modifier = Modifier
                                    .padding(top = 3.dp)
                                    .fillMaxWidth(0.78f),
                                shape = RoundedCornerShape(12.dp),
                            )
                            LaunchedEffect(onClickCertified) {
                                if (onClickCertified) {
                                    elapsedSecond = 180

                                    while (true) {
                                        delay(1000L)
                                        --elapsedSecond
                                        if (elapsedSecond == 0) {
                                            timeDone = true

                                            break
                                        }
                                    }
                                }
                            }
                            LaunchedEffect(Unit) {
                                delay(5000L) // 5초 대기
                                isClicked = true
                            }
                            Button(
                                onClick = {
                                    isWrong = false
                                    timeDone = false
                                    elapsedSecond = 180
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
                                    fontFamily = pretendardVariable,
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
                        if (isWrong) {
                            Text(
                                fontSize = 12.sp,
                                text = "인증번호가 달라요.",
                                color = Color(0xFF999999),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        } //틀렸을 떄 나오는 문구
                        if (timeDone) {
                            Text(
                                fontSize = 12.sp,
                                text = "시간이 초과되었어요. 다시 시도해주세요.",
                                color = Color(0xFF999999),
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        }
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
                            if (email.isNotBlank()) {
                                val fullemail = "$email@$SCHOOL_EMAIL_DOMAIN"

                                //API 성공 후 ->
                                onClickCertified = true
                                isVisible = false
                                isEmailWrong = false
                                elapsedSecond = 180
                            } else {
                                isEmailWrong = true
                            }
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
                            fontFamily = pretendardVariable,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            text = "이메일 인증"
                        )
                    }
                }
                Button(
                    onClick = {
                        if (!timeDone) {
                            if (certifiedNum.isNotBlank() && receiveRealCertifiedNum.isNotBlank()) {
                                if (certifiedNum == receiveRealCertifiedNum) {
                                    navController.navigate(Screen.SignUp3.route)
                                } //여기에 만약 인증이 되면 뭐할건지 로직 추가
                                else {
                                    isWrong = true
                                } //여기에 인증 안된채로 누르면 어떤 반응 할지
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
                        .fillMaxWidth(0.89f)
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

    @Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
    @Composable
    fun SignUpScreenPreview7() {
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

    @Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
    @Composable
    fun SignUpScreenPreview6() {
        DeveryTime_Android2Theme {
            Box(modifier = Modifier.fillMaxSize()) {
                SignUp2Screen(navController = rememberNavController())
            }
        }
    }
