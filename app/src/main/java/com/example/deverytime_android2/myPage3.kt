package com.example.deverytime_android2

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.buttonGray
import com.example.deverytime_android2.ui.theme.mainBlue

@Composable
fun myPage3Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var profileImageUri by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val profileImagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
        ) { uri ->
            if (uri != null) {
                profileImageUri = uri.toString()
            }
        }

    var changedId by remember { mutableStateOf("") }
    var isClicked by remember { mutableStateOf(false) }
    var onVerify by remember { mutableStateOf(false) }
    val buttonColor =
        when {
            isClicked -> mainBlue

            // 버튼 클릭 후 회색
            changedId.isNotEmpty() -> buttonGray

            // 글자가 있으면 파란색
            else -> buttonGray // 글자가 없으면 회색
        }
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier =
                    Modifier
                        .padding(start = 16.dp, top = 50.dp)
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
            Row(modifier = Modifier.padding(top = 20.dp, start = 6.dp)) {
                AsyncImage(
                    model = profileImageUri ?: R.drawable.vector_5,
                    contentDescription = "마이페이지프로필",
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .padding(start = 30.dp)
                            .size(63.dp)
                            .clip(CircleShape)
                            .clickable {
                                profileImagePicker.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly,
                                    ),
                                )
                            },
                )
                Text(
                    text = "$name | $schoolNumber",
                    fontFamily = pretendardVariable,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 12.dp),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column {
                Text(
                    fontSize = 12.sp,
                    text = "이메일",
                    color = buttonGray,
                    modifier = Modifier.padding(start = 22.dp, bottom = 5.dp),
                )
                OutlinedTextField(
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedPlaceholderColor = Color.Transparent,
                            unfocusedPlaceholderColor = buttonGray,
                            errorBorderColor = Color.Red,
                        ),
                    value = "$userEmail",
                    onValueChange = {},
                    readOnly = true,
                    modifier =
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth(1f)
                            .padding(horizontal = 22.dp),
                    shape = RoundedCornerShape(12.dp),
                )
            }

            Spacer(modifier = Modifier.weight(0.15f))

            Column {
                Text(
                    fontSize = 12.sp,
                    text = "아이디",
                    color = buttonGray,
                    modifier = Modifier.padding(start = 22.dp),
                )
                Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    OutlinedTextField(
                        colors =
                            OutlinedTextFieldDefaults.colors(
                                focusedPlaceholderColor = Color.Transparent,
                                unfocusedPlaceholderColor = buttonGray,
                                errorBorderColor = Color.Red,
                            ),
                        placeholder = { Text(text = userName) },
                        value = changedId,
                        onValueChange = {
                            changedId = it
                            if (changedId.isNotBlank() && changedId != userName) {
                                isClicked = true
                            }
                            onVerify = false
                        },
                        modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(top = 6.dp)
                                .height(50.dp)
                                .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    Button(
                        onClick = {
                            // TODO: 백엔드와 연동하여 중복 확인 이후 onVerity를 true로 만들어 변경사항 저장 버튼이 작동 가능하게 한다.

                            isClicked = false
                        },
                        enabled = isClicked,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = buttonColor,
                            ),
                        shape = RoundedCornerShape(12.dp),
                        modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 10.dp)
                                .weight(0.38f)
                                .height(56.dp)
                                .padding(top = 6.dp),
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        Text(
                            fontFamily = pretendardVariable,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            text = "중복확인",
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Visible,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(14f))
        }
        Button(
            onClick = {
                // TODO: 변경사항 저장 백엔드 연동 필요 *추가 수정 필요*
                if (onVerify) {
                    navController.popBackStack()
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
                text = "변경사항 저장",
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun myPage3Preview() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.mypage3),
                contentDescription = "디자인 미리보기",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .alpha(0.3f),
                contentScale = ContentScale.Fit,
            )
            myPage3Screen(navController = rememberNavController())
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun myPage3Preview2() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            myPage3Screen(navController = rememberNavController())
        }
    }
}
