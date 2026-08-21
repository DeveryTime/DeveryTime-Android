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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.Pretendard

@Composable
fun OnBoard5Screen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(top = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 70.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DSM 소통의 길,",
                fontSize = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.W700
            )
            Text(
                text = "그럼 시작해볼까요?",
                fontSize = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.W700
            )
            Image(
                contentDescription = "panda",
                painter = painterResource(R.drawable.ic_onboarding5),
                modifier = Modifier

            )
        }
        Column(
            modifier = Modifier
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(),
            ) {
                Text(
                    text = "만약 계정이 있으신가요?",
                    fontFamily = Pretendard,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .padding(bottom = 7.dp,),
                    color = Color(0xFFb1b1b1)
                )
                Text(
                    text = "로그인",
                    fontFamily = Pretendard,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier
                        .padding(start = 4.dp, bottom = 7.dp)
                        .clickable{(navController.navigate(Screen.OnBoard1.route))},
                    color = Color(0xFF3469f9),
                    textDecoration = TextDecoration.Underline
                )
            }
            Button(
                onClick = { navController.navigate(Screen.OnBoard2.route) },
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
                    text = "시작하기",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun OnBoard5() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.onboarding5),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Fit
            )
            OnBoard5Screen(navController = rememberNavController())
        }

    }
}
