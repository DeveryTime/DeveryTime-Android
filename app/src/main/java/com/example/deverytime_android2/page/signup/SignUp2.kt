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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.deverytime_android2.page.theme.buttonGray
import com.example.deverytime_android2.page.theme.mainBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val SCHOOL_EMAIL_DOMAIN = "dsm.hs.kr"

@Composable
fun SignUp2Screen(
    navController: NavHostController,
    signUpViewModel: SignUpViewModel,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") } // 이메일
    var certifiedNum by remember { mutableStateOf("") } // 사용자가 입력한 인증번호
    var onClickCertified by remember { mutableStateOf(false) } // 인증버튼이 눌렸는지 안 눌렸는지
    var isVisible by remember { mutableStateOf(true) } // 보이는지 안보이는지
    var isClicked by remember { mutableStateOf(false) } // 재전송 버튼 색상 변경 변수
    val scope = rememberCoroutineScope() // 5초 카운트 변수
    var elapsedSecond by remember { mutableStateOf(0) } // 카운트 업 변수
    var isWrong by remember { mutableStateOf(false) } // 인증번호가 틀렸을 때 true로 바뀌는 변수
    var isEmailWrong by remember { mutableStateOf(false) } // 이메일 형식이 틀렸을 때 true로 바뀌는 변수
    var timeDone by remember { mutableStateOf(false) } // 시간이 다 지났는지 확인하는 변수
    var timerRestartKey by remember { mutableStateOf(0) } // 코루틴 키값
    val emailVerificationState by
    signUpViewModel.emailVerificationState.collectAsState()

    LaunchedEffect(emailVerificationState) {
        when (emailVerificationState) {
            is EmailVerificationUiState.CodeSent -> {
                onClickCertified = true
                isVisible = false
                isEmailWrong = false
                isWrong = false
                timeDone = false
                timerRestartKey++
            }

            is EmailVerificationUiState.Verified -> {
                val fullEmail = "$email@$SCHOOL_EMAIL_DOMAIN"

                signUpViewModel.updateEmail(fullEmail)
                signUpViewModel.resetEmailVerificationState()
                navController.navigate(Screen.SignUp3.route)
            }

            is EmailVerificationUiState.Error -> {
                if (onClickCertified) {
                    isWrong = true
                } else {
                    isEmailWrong = true
                }
            }

            else -> Unit
        }
    }

    // 포커스 매니저
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // 경과시간 계산 포맷
    val minutes = elapsedSecond / 60 // 분
    val seconds = elapsedSecond % 60 // 초
    val formattedTime = "%02d:%02d".format(minutes, seconds)
    Box(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = {
                navController.popBackStack()
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
            painter = painterResource(id = R.drawable.frame_68),
            contentDescription = "디자인 미리보기",
            modifier =
                Modifier
                    .width(150.dp)
                    .padding(start = 8.dp, top = 52.dp),
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 120.dp)
                    .align(Alignment.TopCenter),
        ) {
            Text(
                text = "이메일을 인증해주세요!",
                fontSize = 23.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = pretendardVariable,
                modifier = Modifier.padding(start = 3.dp, bottom = 42.dp),
            )

            // 이메일 입력창
            Text(
                fontSize = 12.sp,
                text = "이메일",
                color = buttonGray,
            )
            OutlinedTextField(
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedPlaceholderColor = Color.Transparent,
                        unfocusedPlaceholderColor = buttonGray,
                    ),
                placeholder = { Text(text = "이메일") },
                value = email,
                onValueChange = { input ->
                    email =
                        input
                            .substringBefore("@")
                            .filter { it.isDigit() }
                            .take(8) // ex: 20261114 총 8자
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
                modifier =
                    Modifier
                        .padding(top = 3.dp)
                        .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                isError = isEmailWrong,
                keyboardOptions =
                    KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                    ),
            )
            if (isEmailWrong) {
                Text(
                    text = "잘못된 이메일 형식입니다.",
                    fontSize = 12.sp,
                    color = buttonGray,
                    modifier = Modifier.padding(top = 5.dp),
                )
            }
            Spacer(modifier = Modifier.height(9.dp))
            if (onClickCertified && email.isNotEmpty()) {
                Column {
                    // 인증번호 입력창
                    Text(
                        fontSize = 12.sp,
                        text = "인증번호",
                        color = buttonGray,
                    )
                    Row(modifier = Modifier.height(62.dp)) {
                        OutlinedTextField(
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedPlaceholderColor = Color.Transparent,
                                    unfocusedPlaceholderColor = buttonGray,
                                ),
                            placeholder = { Text(text = "인증번호") },
                            trailingIcon = {
                                Text(
                                    modifier = Modifier.padding(end = 12.dp),
                                    text =
                                        "%02d:%02d".format(
                                            elapsedSecond / 60,
                                            elapsedSecond % 60,
                                        ),
                                    fontSize = 16.sp,
                                    color = buttonGray,
                                )
                            },
                            value = certifiedNum,
                            onValueChange = { newValue ->
                                certifiedNum =
                                    newValue
                                        .take(6) // 최대 6자 제한
                            },
                            modifier =
                                Modifier
                                    .padding(top = 3.dp)
                                    .fillMaxWidth(0.78f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions =
                                KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Number,
                                ),
                            keyboardActions =
                                KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    },
                                ),
                        )
                        LaunchedEffect(onClickCertified, timerRestartKey) {
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
                                timerRestartKey++

                                if (isClicked) {
                                    val fullEmail = "$email@$SCHOOL_EMAIL_DOMAIN"

                                    signUpViewModel.sendEmailVerification(fullEmail)

                                    isClicked = false

                                    scope.launch {
                                        delay(5000L)
                                        isClicked = true
                                    }
                                }
                            },
                            enabled = isClicked,
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        if (isClicked) {
                                            mainBlue
                                        } else {
                                            buttonGray
                                        },
                                ),
                            shape = RoundedCornerShape(18.dp),
                            modifier =
                                Modifier
                                    .padding(start = 10.dp)
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
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    if (isWrong && !timeDone) {
                        Text(
                            fontSize = 12.sp,
                            text = "인증번호가 달라요.",
                            color = buttonGray,
                            modifier = Modifier.padding(top = 5.dp),
                        )
                    } // 틀렸을 떄 나오는 문구
                    if (timeDone) {
                        Text(
                            fontSize = 12.sp,
                            text = "시간이 초과되었어요. 다시 시도해주세요.",
                            color = buttonGray,
                            modifier = Modifier.padding(top = 5.dp),
                        )
                    }
                }
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 10.dp)) {
                Text(
                    fontSize = 14.sp,
                    text = "계정이 있으신가요?",
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
                            .padding(horizontal = 3.dp)
                            .clickable {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.SignUp1.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                )
            }
            Box(modifier = Modifier, Alignment.BottomCenter) {
                if (isVisible) {
                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                val fullEmail = "$email@$SCHOOL_EMAIL_DOMAIN"

                                signUpViewModel.sendEmailVerification(fullEmail)

                                onClickCertified = true
                                isVisible = false
                                isEmailWrong = false
                                elapsedSecond = 180
                            } else {
                                isEmailWrong = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
                        shape = RoundedCornerShape(23.dp),
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(bottom = 33.dp, start = 18.dp, end = 18.dp) // 33
                                .height(54.dp)
                                .zIndex(1f),
                    ) {
                        Text(
                            fontFamily = pretendardVariable,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            text = "이메일 인증",
                        )
                    }
                }
                Button(
                    onClick = {
                        if (timeDone) {
                            isWrong = true
                        } else if (certifiedNum.isNotBlank()) {
                            val fullEmail = "$email@$SCHOOL_EMAIL_DOMAIN"

                            isWrong = false
                            signUpViewModel.verifyEmail(
                                email = fullEmail,
                                code = certifiedNum,
                            )
                        } else {
                            isWrong = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
                    shape = RoundedCornerShape(23.dp),
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter) // Box 안에서 하단 중앙
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
}
