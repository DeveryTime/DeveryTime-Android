package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.page.theme.buttonGray
import com.example.deverytime_android2.page.theme.mainBlue

@Composable
fun SignUp4Screen(
    navController: NavHostController,
    signUpViewModel: SignUpViewModel,
    modifier: Modifier = Modifier,
) {
    // 백엔드에 있는 계정인지 true false 요청하고 true면 isIdExisting를 true로 변경
    // 추후 변경 예정

    var id by remember { mutableStateOf("") }
    var isClicked by remember { mutableStateOf(false) }
    var isIdExisting by remember { mutableStateOf(false) }
    var isWrong by remember { mutableStateOf(false) }
    val buttonColor =
        when {
            isClicked -> buttonGray

            // 버튼 클릭 후 회색
            id.isNotEmpty() -> mainBlue

            // 글자가 있으면 파란색
            else -> buttonGray // 글자가 없으면 회색
        }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val uiState by signUpViewModel.uiState.collectAsState()

    val usernameCheckState by
    signUpViewModel.usernameCheckState.collectAsState()

    var isCheckRequired by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SignUpUiState.Success -> {
                signUpViewModel.clearForm()
                signUpViewModel.resetUiState()

                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.SignUp1.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            is SignUpUiState.Error -> {
                isIdExisting =
                    state.errorCode == "USERNAME_ALREADY_EXISTS"
            }

            else -> Unit
        }
    }

    LaunchedEffect(usernameCheckState) {
        when (val state = usernameCheckState) {
            is UsernameCheckUiState.Available -> {
                isClicked = true
                isIdExisting = false
                isCheckRequired = false
            }

            is UsernameCheckUiState.Error -> {
                isClicked = false
                isIdExisting =
                    state.errorCode == "USERNAME_ALREADY_EXISTS"
            }

            else -> Unit
        }
    }
    Box(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
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
            painter = painterResource(id = R.drawable.frame_70),
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
                    .padding(top = 120.dp, start = 18.dp, end = 18.dp)
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            Column {
                Text(
                    text = "사용자님의 모습이 궁금해요!",
                    fontSize = 23.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(55.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "사용자 프로필",
                        modifier =
                            Modifier
                                .width(200.dp)
                                .align(Alignment.BottomCenter),
                        contentScale = ContentScale.Fit,
                    )
                    Button(
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = { /* 이미지 업로드 로직 */ },
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.frame_83),
                            contentDescription = "디자인 미리보기",
                            modifier = Modifier.padding(start = 150.dp, bottom = 2.dp),
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.CenterHorizontally),
            ) {
                // 아이디 입력창
                Text(
                    fontSize = 12.sp,
                    text = "아이디",
                    color = buttonGray,
                )
                Row {
                    OutlinedTextField(
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedPlaceholderColor = Color.Transparent,
                                unfocusedPlaceholderColor = buttonGray,
                                errorBorderColor = Color.Red,
                            ),
                        placeholder = { Text(text = "우아한 강아지") },
                        value = id,
                        onValueChange = { newValue ->
                            id = newValue.take(10)

                            isClicked = false
                            isWrong = false
                            isIdExisting = false
                            isCheckRequired = false

                            signUpViewModel.resetUsernameCheckState()
                            signUpViewModel.resetUiState()
                        },
                        singleLine = true,
                        keyboardOptions =
                            KeyboardOptions(
                                imeAction = ImeAction.Done,
                            ),
                        keyboardActions =
                            KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                },
                            ),
                        modifier = Modifier.padding(top = 3.dp).weight(1f),
                        shape = RoundedCornerShape(12.dp),

                    )
                    Button(
                        onClick = {
                            signUpViewModel.checkUsername(id)
                        },
                        enabled =
                            id.isNotBlank() &&
                                    usernameCheckState !is UsernameCheckUiState.Loading,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = buttonColor,
                            ),
                        shape = RoundedCornerShape(12.dp),
                        modifier =
                            Modifier
                                .padding(start = 10.dp)
                                .weight(0.32f)
                                .height(56.dp)
                                .padding(top = 6.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(
                            fontFamily = pretendardVariable,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            text =
                                when (usernameCheckState) {
                                    is UsernameCheckUiState.Loading ->
                                        "확인 중"

                                    is UsernameCheckUiState.Available ->
                                        "확인 완료"

                                    else ->
                                        "중복확인"
                                },
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                if (isIdExisting) {
                    Text(
                        text = "이미 사용 중인 아이디입니다.",
                        color = buttonGray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 5.dp),
                    )
                }
                    if (isWrong) {
                        Text(
                            text = "아이디를 입력해 주세요.",
                            color = buttonGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 5.dp),
                        )
                    }
                    if (isCheckRequired) {
                        Text(
                            text = "아이디 중복확인을 해주세요.",
                            color = buttonGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 5.dp),
                        )
                    }

                    val signUpError = uiState as? SignUpUiState.Error

                    if (
                        signUpError != null &&
                        signUpError.errorCode != "USERNAME_ALREADY_EXISTS"
                    ) {
                        Text(
                            text = signUpError.message,
                            color = buttonGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 5.dp),
                        )
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
                Button(
                    onClick = {
                        when {
                            id.isBlank() -> {
                                isWrong = true
                                isCheckRequired = false
                            }

                            usernameCheckState !is UsernameCheckUiState.Available -> {
                                isWrong = false
                                isCheckRequired = true
                            }

                            else -> {
                                isWrong = false
                                isCheckRequired = false
                                signUpViewModel.updateUsername(id)
                                signUpViewModel.signUp()
                            }
                        }
                    },
                    enabled = uiState !is SignUpUiState.Loading,
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
                        text =
                            if (uiState is SignUpUiState.Loading) {
                                "가입 중..."
                            } else {
                                "다음"
                            },
                    )
                }
            }
        }
    }
}
