package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.Pretendard
import kotlinx.coroutines.delay

@Composable
fun OnBoard1Screen(navController: NavHostController) {
    //TODO: 로딩 후 이동
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate(Screen.OnBoard2.route) {
            popUpTo("splash") { inclusive = true }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF5581FA))) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Image(
                    contentDescription = "null",
                    painter = painterResource(R.drawable.ic_onboarding1),
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 25.dp)
                )
                Text(
                    text = stringResource(R.string.onboard1_1),
                    fontSize = 14.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                )
                Text(
                    text = stringResource(R.string.onboard1_2),
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
