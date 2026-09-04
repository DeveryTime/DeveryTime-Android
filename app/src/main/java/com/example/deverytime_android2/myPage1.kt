package com.example.deverytime_android2

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.buttonGray
import com.example.deverytime_android2.ui.theme.mainBlue
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: 백엔드에서 연동 해와야 함
public val name = "박XX"
public val userName = "발랄한 바둑이"
val schoolNumber = 1107
val userEmail = "deverytime2026@gmail.com"

private fun formatTime(time: String): String {
    val inputFormat =
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale.KOREA,
        )

    val outputFormat =
        SimpleDateFormat(
            "yy.MM.dd",
            Locale.KOREA,
        )

    return runCatching {
        val date = inputFormat.parse(time)
        date?.let { outputFormat.format(it) } ?: time
    }.getOrElse {
        time
    }
}

@Composable
private fun postItem(
    title: String,
    time: String,
    like: Int,
    showTopBorder: Boolean = false,
) {
    val changeTime = formatTime(time)
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .clickable {
                    // TODO:페이지 생성 후 연계 필요
                },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.8.dp)
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()

                        if (showTopBorder) {
                            drawLine(
                                color = buttonGray,
                                start = Offset(0f, strokeWidth / 2),
                                end = Offset(size.width, strokeWidth / 2),
                                strokeWidth = strokeWidth,
                            )
                        }

                        drawLine(
                            color = buttonGray,
                            start = Offset(0f, size.height - strokeWidth / 2),
                            end = Offset(size.width, size.height - strokeWidth / 2),
                            strokeWidth = strokeWidth,
                        )
                    },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.padding(horizontal = 21.dp).weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.65.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF000000),
                )
                Text(
                    text = changeTime,
                    fontSize = 12.sp,
                    fontFamily = pretendardVariable,
                    color = buttonGray,
                )
            }
            Row(
                modifier = Modifier.padding(end = 21.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.uil_thumbs_up),
                    contentDescription = stringResource(id = R.string.thumbs_up),
                    contentScale = ContentScale.Fit,
                    modifier =
                        Modifier
                            .padding(end = 5.dp)
                            .size(18.dp),
                )
                Text(
                    text = like.toString(),
                    fontSize = 12.sp,
                    fontFamily = pretendardVariable,
                    color = buttonGray,
                )
            }
        }
    }
}

@Composable
fun myPage1Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.deverytime_logo),
            contentDescription = "로고",
            modifier =
                Modifier
                    .size(90.dp)
                    .align(Alignment.End)
                    .padding(top = 40.dp, end = 8.dp)
                    .clickable {
                        // TODO:이미지 교체 가능하게
                    },
        )
        Row(modifier = Modifier.padding(top = 10.dp)) {
            Image(
                painter = painterResource(id = R.drawable.vector_5),
                contentDescription = "마이페이지프로필",
                modifier =
                    Modifier
                        .padding(start = 30.dp),
            )
            Column(modifier = Modifier.padding(start = 16.dp).align(Alignment.CenterVertically)) {
                Text(
                    text = "$name | $schoolNumber",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                )
                Text(
                    text = userName,
                    fontSize = 14.sp,
                    color = buttonGray,
                    fontFamily = pretendardVariable,
                )
                Text(
                    text = userEmail,
                    fontSize = 14.sp,
                    fontFamily = pretendardVariable,
                )
            }
        }
        Spacer(modifier = Modifier.height(33.dp))
        Column {
            Row(
                modifier =
                    Modifier
                        .padding(start = 22.dp)
                        .clickable {
                            navController.navigate(Screen.MyPage2.route)
                        },
            ) {
                Text(
                    text = "내가 쓴 글",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                )
                Icon(
                    modifier =
                        Modifier
                            .padding(start = 4.dp)
                            .scale(scaleX = -1f, scaleY = 1f)
                            .size(16.dp)
                            .align(Alignment.CenterVertically),
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "화살표 버튼",
                )
            }
            LazyColumn(
                modifier =
                    Modifier
                        .padding(top = 19.dp)
                        .clickable {
                            navController.navigate(Screen.MyPage2.route)
                        },
            ) {
                item {
                    postItem(title[0], time[0], like[0], true)
                }
                item {
                    postItem(title[1], time[1], like[1])
                }
                item {
                    postItem(title[2], time[2], like[2])
                }
            }
        }
        Column(modifier = Modifier.padding(start = 22.dp, top = 50.dp)) {
            Text(
                text = "MY",
                fontFamily = pretendardVariable,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                modifier =
                    Modifier
                        .clickable {
                            navController.navigate(Screen.MyPage3.route)
                        },
                text = "프로필 수정",
                fontFamily = pretendardVariable,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "로그아웃",
                fontFamily = pretendardVariable,
                fontSize = 18.sp,
                modifier =
                    Modifier
                        .clickable {
                            // TODO: 토큰 연동 이후
                        },
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun myPage1Preview() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.mypage1),
                contentDescription = "디자인 미리보기",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .alpha(0.3f),
                contentScale = ContentScale.Fit,
            )
            myPage1Screen(navController = rememberNavController())
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun myPage1Preview2() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            myPage1Screen(navController = rememberNavController())
        }
    }
}
