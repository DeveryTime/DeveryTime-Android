package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.CommonButton
import com.example.deverytime_android2.ui.theme.Style

@Composable
fun OnBoard5Screen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(108.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.onboard5_1),
                style = Style.Title,
            )
            Text(
                text = stringResource(R.string.onboard5_2),
                style = Style.Title,
            )
            Image(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_onboarding5),
            )
            Spacer(Modifier.height(70.dp))
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {
                Text(
                    text = stringResource(R.string.onboard5_3),
                    style = Style.Caption,
                    color = Color(0xFFb1b1b1)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = stringResource(R.string.login),
                    style = Style.Caption,
                    modifier = Modifier
                        .padding(bottom = 7.dp)
                        //TODO:로그인 경로 추가
                        .clickable {},
                    color = Color(0xFF3469f9),
                    textDecoration = TextDecoration.Underline
                )
            }
            CommonButton(
                text = stringResource(R.string.start),
                //TODO: 회원가입 경로 추가
                onClick = {},
            )
        }
    }
}
