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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.buttonGray
import java.text.SimpleDateFormat
import java.util.Locale

data class Post(
    val title: String,
    val time: String,
    val like: Int,
)

val posts =
    List(24) { index ->
        Post(
            title =
                if (index % 3 == 0) {
                    "오늘 저녁은 치킨이다"
                } else {
                    "집에가 인것은 길이 측정을 위해서 하는 긴 글입니다."
                },
            time = "2026-08-04T12:30:00",
            like = 5,
        )
    }

public fun formatTime(time: String): String {
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
public fun PostItem(
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
                    .height(49.dp)
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
                    text = post.title,
                    fontSize = 15.65.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = pretendardVariable,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
                    text = post.like.toString(),
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
                navController.popBackStack()
            },
            modifier =
                Modifier
                    .padding(start = 12.dp, top = 45.dp)
                    .size(32.dp),
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape,
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
                    .padding(top = 100.dp)
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
                itemsIndexed(
                    posts,
                ) { index, item ->
                    PostItem(
                        post = posts[index],
                        showTopBorder = index == 0,
                    )
                }
            }
        }
    }
}
