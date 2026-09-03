package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.draw
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

public val title =
    listOf(
        "오늘 저녁은 치킨이다",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "오늘 저녁은 치킨이다",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "오늘 저녁은 치킨이다",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "오늘 저녁은 치킨이다",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "오늘 저녁은 치킨이다",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "오늘 저녁은 치킨이다",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "오늘 저녁은 치킨이다",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "오늘 저녁은 치킨이다",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
        "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다.",
    )

public val time =
    listOf<String>(
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
        "2026-08-04T12:30:00",
    )

public val like =
    listOf(
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
        5,
    )

// TODO: indexSize는 추후 백엔드에서 받아오는 값으로 변경 필요
val indexSize = title.size - 2

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
fun myPage2Screen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier =
                Modifier
                    .padding(start = 12.dp, top = 45.dp)
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
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.88f)
                    .align(Alignment.BottomCenter),
        ) {
            Text(
                text = "내가 쓴 글",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier =
                    Modifier
                        .padding(horizontal = 17.dp),
                fontFamily = pretendardVariable,
            )
            Spacer(modifier = Modifier.height(25.dp))
            LazyColumn {
                item {
                    postItem(title[0], time[0], like[0], true)
                }
                items(indexSize) { index ->
                    postItem(title[index + 1], time[index + 1], like[index + 1])
                }
                item {
                    postItem(title.last(), time.last(), like.last())
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun myPage2Preview3() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.mypage2),
                contentDescription = "디자인 미리보기",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .alpha(0.3f),
                contentScale = ContentScale.Fit,
            )
            myPage2Screen(navController = rememberNavController())
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun myPage1Preview4() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            myPage2Screen(navController = rememberNavController())
        }
    }
}

@Preview
@Composable
fun post() {
    DeveryTime_Android2Theme {
        postItem(
            title = title[0],
            time = time[0],
            like = like[0],
        )
    }
}
