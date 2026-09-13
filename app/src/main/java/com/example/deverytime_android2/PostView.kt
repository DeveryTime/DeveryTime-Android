package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.buttonGray
import java.text.SimpleDateFormat
import java.util.Locale

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

@Composable
fun PostViewScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val changeTime = formatTime(time)

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
        Row(modifier = Modifier.padding(top = 16.dp)) {
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
                    text = userName,
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
