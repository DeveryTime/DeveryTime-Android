package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.buttonGray
import com.example.deverytime_android2.ui.theme.commentTextFieldColor
import java.text.SimpleDateFormat
import java.util.Locale

val otherUserName = "홍길동"
val time = "2026-08-04T12:30:00"
val view = 3

private fun formatTime(time: String): String {
    val inputFormat =
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale.KOREA,
        )

    val outputFormat =
        SimpleDateFormat(
            "yyyy년 MM월 dd일 HH:mm",
            Locale.KOREA,
        )

    return runCatching {
        val date = inputFormat.parse(time)
        date?.let { outputFormat.format(it) } ?: time
    }.getOrElse {
        time
    }
}

private fun yearMonthDay(time: String): String {
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
public fun PostItem2(
    post: Post,
    showTopBorder: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val changeTime = formatTime(post.time)
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
                    .height(72.dp)
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
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    Row(modifier = Modifier.padding(top = 10.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.frame_799),
                            contentDescription = "프로필 사진",
                            modifier =
                                Modifier
                                    .padding(end = 5.dp)
                                    .size(24.dp),
                        )
                        Text(
                            text = post.otherUserName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = pretendardVariable,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = yearMonthDay(post.time),
                            fontSize = 12.sp,
                            fontFamily = pretendardVariable,
                            color = buttonGray,
                            modifier =
                                Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 5.dp),
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.ic_comment_menu_kebob),
                        contentDescription = "케밥",
                        modifier =
                            Modifier
                                .size(14.dp)
                                .align(Alignment.CenterVertically),
                    )
                }
                Text(
                    text = post.comment,
                    fontSize = 14.sp,
                    fontFamily = pretendardVariable,
                    modifier =
                        Modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun PostViewScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val changeTime = formatTime(time)

    Box {
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(start = 15.dp, top = 45.dp),
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier =
                        Modifier
                            .size(40.dp),
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
                                .size(40.dp)
                                .align(Alignment.CenterVertically),
                    )
                }
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
            ) {
                // TODO: 사진을 백엔드에서 가져와야함
                Image(
                    painter = painterResource(id = R.drawable.frame_799),
                    contentDescription = "마이페이지 프로필",
                    modifier =
                        Modifier
                            .padding(start = 28.dp),
                )
                Column(
                    modifier =
                        Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.CenterVertically),
                ) {
                    Text(
                        text = otherUserName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = pretendardVariable,
                    )
                    Text(
                        text = "$changeTime - $view 조회",
                        fontSize = 13.sp,
                        fontFamily = pretendardVariable,
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_kebab),
                        contentDescription = "케밥",
                        modifier =
                            Modifier
                                .padding(end = 20.dp, top = 6.dp)
                                .size(16.dp)
                                .align(Alignment.TopEnd),
                    )
                }
            }
            Text(
                text = "저메추",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = pretendardVariable,
                modifier =
                    Modifier
                        .padding(start = 26.dp, top = 24.dp)
                        .fillMaxWidth(),
            )
            Text(
                text = "제곧내",
                fontSize = 18.sp,
                fontFamily = pretendardVariable,
                modifier =
                    Modifier
                        .padding(start = 25.dp, top = 18.dp)
                        .fillMaxWidth(),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier =
                        Modifier
                            .padding(top = 30.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.uil_thumbs_up),
                        contentDescription = stringResource(id = R.string.thumbs_up),
                        modifier =
                            Modifier
                                .padding(start = 26.dp)
                                .size(20.dp),
                    )
                    Text(
                        text = "6",
                        fontSize = 15.sp,
                        fontFamily = pretendardVariable,
                        modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 5.dp),
                    )
                }
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, start = 30.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_speech_bubble_rtl),
                        contentDescription = "댓글",
                        modifier =
                            Modifier
                                .size(16.dp)
                                .align(Alignment.CenterVertically),
                    )
                    Text(
                        text = "8",
                        fontSize = 15.sp,
                        fontFamily = pretendardVariable,
                        modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 6.dp),
                    )
                }
            }
            LazyColumn(
                modifier =
                    Modifier
                        .padding(top = 30.dp)
                        .fillMaxSize(),
            ) {
                itemsIndexed(
                    posts,
                ) { index, item ->
                    PostItem2(
                        post = posts[index],
                        showTopBorder = index == 0,
                    )
                }
            }
        }
        Row(
            modifier =
                Modifier
                    .imePadding()
                    .height(80.dp)
                    .background(Color.White)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp, start = 10.dp)
        ) {
            Row(
                modifier =
                    Modifier,
            ) {
                BasicTextField(
                    value = "",
                    onValueChange = {},
                    singleLine = true,
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(35.dp)
                            .background(commentTextFieldColor, CircleShape)
                            .padding(horizontal = 14.dp),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            Text(
                                "댓글",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontFamily = pretendardVariable,
                            )
                            innerTextField()
                        }
                    },
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_comment_post_button),
                    contentDescription = "댓글 전송",
                    modifier =
                        Modifier
                            .padding(horizontal = 15.dp)
                            .size(24.dp)
                            .align(Alignment.CenterVertically),
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun GreetingPrevie1w() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.post_view),
                contentDescription = "디자인 미리보기",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .alpha(0.3f),
                contentScale = ContentScale.Fit,
            )
        }
        PostViewScreen(navController = NavHostController(LocalContext.current))
    }
}

@Preview
@Composable
fun post() {
    DeveryTime_Android2Theme {
        PostItem2(
            post =
                Post(
                    title = "오늘 저녁은 치킨이다",
                    time = "2026-08-04T12:30:00",
                    like = 5,
                    otherUserName = "홍길동",
                    comment = "마라탕",
                ),
        )
    }
}
