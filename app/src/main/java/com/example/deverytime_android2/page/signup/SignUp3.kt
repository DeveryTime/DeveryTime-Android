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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.page.theme.buttonGray
import com.example.deverytime_android2.page.theme.mainBlue

@Composable
fun SignUp3Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var password by remember { mutableStateOf("") }
    var recheckNumber by remember { mutableStateOf("") }
    var isWrong by remember { mutableStateOf(false) }

    // 포커스 매니저
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
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

        Column(
            modifier =
                Modifier
                    .padding(top = 120.dp)
                    .align(Alignment.TopCenter),
        ) {
            Text(
                text = "비밀번호를 알려주세요!",
                fontSize = 23.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = pretendardVariable,
                modifier = Modifier.padding(start = 22.dp),
            )

            Column(
                modifier =
                    Modifier
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
                            focusedPlaceholderColor = Color.Transparent,
                            unfocusedPlaceholderColor = buttonGray,
                            errorBorderColor = Color.Red,
                        ),
                    placeholder = { Text(text = "비밀번호") },
                    value = password,
                    onValueChange = { newValue ->
                        password =
                            newValue
                                .take(20) // 최대 20자 제한
                    },
                    singleLine = true,
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                    keyboardActions =
                        KeyboardActions(
                            onNext = {
                                keyboardController?.hide()
                                focusManager.moveFocus(FocusDirection.Down)
                            },
                        ),
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
                            focusedPlaceholderColor = Color.Transparent,
                            unfocusedPlaceholderColor = buttonGray,
                            errorBorderColor = Color.Red,
                        ),
                    placeholder = { Text(text = "비밀번호 확인") },
                    value = recheckNumber,
                    onValueChange = { newValue ->
                        recheckNumber =
                            newValue
                                .take(20) // 최대 20자 제한
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
