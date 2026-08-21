package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun OnBoard3Screen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(end = 30.dp, top = 108.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "데브리타임은 DSM 학생들을 위한",
                fontSize = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.W700
            )
            Text(
                text = "교내 커뮤니티 서비스에요.",
                fontSize = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.W700
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 140.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                contentDescription = "panda",
                painter = painterResource(R.drawable.ic_onboarding3),
                modifier = Modifier
                        .padding(vertical = 60.dp),
            )
            Image(
                contentDescription = "panda",
                painter = painterResource(R.drawable.ic_kebab3),
            )
        }
        Column(
            modifier = Modifier
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                onClick = { navController.navigate(Screen.OnBoard4.route) },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                contentPadding = PaddingValues(vertical = 18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3469F9),
                    contentColor = Color(0xFFFFFFFF)
                )
            ) {
                Text(
                    text = "다음",
                    fontSize = 16.sp
                )
            }
        }
    }
}



@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun OnBoard3() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.onboarding3),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Fit
            )
            OnBoard3Screen(navController = rememberNavController())
        }

    }
}
