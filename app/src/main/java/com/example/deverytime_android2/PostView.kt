package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.buttonGray
import com.example.deverytime_android2.ui.theme.commentTextFieldColor
import com.example.deverytime_android2.ui.theme.nonprofile
import java.text.SimpleDateFormat
import java.util.Locale

val otherUserName = "홍길동"
val view = 3


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
public fun commentItem2(
    post: Post,
    showTopBorder: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val changeTime = formatTime(post.time)
    var commentExpanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Box(
        modifier =
            Modifier
                .fillMaxSize(),
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
                            tint = nonprofile,
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
                                .align(Alignment.CenterVertically),
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
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    commentExpanded = !commentExpanded
                                },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_comment_menu_kebob),
                            contentDescription = "케밥",
                            modifier =
                                Modifier
                                    .size(14.dp),
                        )
                        DropdownMenu(
                            expanded = commentExpanded,
                            onDismissRequest = { commentExpanded = false },
                            containerColor = Color.White,
                        ) {
                            DropdownMenuItem(
                                text = { Text("신고하기") },
                                onClick = {
                                    uriHandler.openUri(
                                        "https://docs.google.com/forms/d/e/1FAIpQLSdZfb16smuoFx3K4JUiB-dqX5hKLywfr2FcyAI4KqWuSYdLZg/viewform?usp=header",
                                    )
                                },
                            )
                        }
                    }
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
    post: Post,
    modifier: Modifier = Modifier,
) {
    val changeTime = formatTime(post.time, "yyyy년 MM월 dd일 HH:mm")
    var expanded by remember { mutableStateOf(false) }
    var comment by remember { mutableStateOf("") }
    val comments =
        remember(post.id) {
            mutableStateListOf(post.comment)
        }
    val uriHandler = LocalUriHandler.current

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
                        text = post.otherUserName,
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
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                            },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_kebab),
                        contentDescription = "케밥",
                        modifier =
                            Modifier
                                .padding(end = 20.dp, top = 6.dp)
                                .size(16.dp)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    expanded = !expanded
                                },
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = Color.White,
                    ) {
                        DropdownMenuItem(
                            text = { Text("신고하기") },
                            onClick = {
                                uriHandler.openUri(
                                    "https://docs.google.com/forms/d/e/1FAIpQLSeLaXHB-Wo9VVqcbNtGBlzQtL5rscli2KoiMpWsRs277_8Qbw/viewform",
                                )
                            },
                        )
                    }
                }
            }
            Text(
                text = post.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = pretendardVariable,
                modifier =
                    Modifier
                        .padding(start = 26.dp, top = 24.dp)
                        .fillMaxWidth(),
            )
            Text(
                text = post.content,
                fontSize = 18.sp,
                fontFamily = pretendardVariable,
                modifier =
                    Modifier
                        .padding(top = 18.dp)
                        .padding(horizontal = 25.dp)
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
                                .size(20.dp)
                                .clickable {
                                },
                    )
                    Text(
                        text = post.like.toString(),
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
                    comments,
                ) { index, commentText ->
                    commentItem2(
                        post = post.copy(comment = commentText),
                        showTopBorder = index == 0,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
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
                    .padding(bottom = 20.dp, start = 10.dp, top = 10.dp),
        ) {
            Row(
                modifier =
                Modifier,
            ) {
                BasicTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    singleLine = true,
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(35.dp)
                            .background(commentTextFieldColor, CircleShape)
                            .padding(horizontal = 14.dp),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (comment.isEmpty()) {
                                Text(
                                    text = "댓글",
                                    fontSize = 12.sp,
                                    fontFamily = pretendardVariable,
                                )
                            }
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
                            .align(Alignment.CenterVertically)
                            .clickable(enabled = comment.isNotBlank()) {
                                comments.add(comment.trim())
                                // TODO: submittedComment를 서버에 전송
                                comment = ""
                            },
                )
            }
        }
    }
}
