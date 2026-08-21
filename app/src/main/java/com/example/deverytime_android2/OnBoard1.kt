package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.Pretendard

@Composable
fun OnBoard1Screen(navController: NavHostController) {
    Box (modifier = Modifier.fillMaxSize().background(Color(0xFF5581FA))){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Image(
                    contentDescription = "panda",
                    painter = painterResource(R.drawable.ic_onboarding1),
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .clickable{
                            (navController.navigate(Screen.OnBoard2.route))
                        }
                )
                Text(
                    text = "Devery Time",
                    fontSize = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 25.dp)
                )
                Text(
                    text = "카테고리별 게시글과",
                    fontSize = 14.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                )
                Text(
                    text = "댓글로 연결되는 DSM커뮤니티",
                    fontSize = 14.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 44.dp)
                )
            }

        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun OnBoard1() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.onboarding1),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Fit
            )
            OnBoard1Screen(navController = rememberNavController())
        }

    }
}
