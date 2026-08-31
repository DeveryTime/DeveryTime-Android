package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.Pretendard

@Composable
fun OnBoard5Screen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(110.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 70.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.onboard5_1),
                fontSize = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.W700
            )
            Text(
                text = stringResource(R.string.onboard5_2),
                fontSize = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.W700
            )
            Image(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_onboarding5),
            )
        }
        Column(
            modifier = Modifier
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {
                Text(
                    text = stringResource(R.string.onboard5_3),
                    fontFamily = Pretendard,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .padding(bottom = 7.dp),
                    color = Color(0xFFb1b1b1)
                )
                Text(
                    text = stringResource(R.string.login),
                    fontFamily = Pretendard,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier
                        .padding(start = 4.dp, bottom = 7.dp)
                        //TODO:로그인 경로 추가
                        .clickable {},
                    color = Color(0xFF3469f9),
                    textDecoration = TextDecoration.Underline
                )
            }
            Button(
                //TODO:회원가입 경로 추가
                onClick = {},
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
                    text = stringResource(R.string.start),
                    fontSize = 16.sp
                )
            }
        }
    }
}