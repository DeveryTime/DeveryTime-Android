package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.Pretendard

@Composable
fun OnBoard3Screen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(end = 30.dp, top = 108.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.onboard3_1),
                fontSize = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.W700
            )
            Text(
                text = stringResource(R.string.onboard3_2),
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
                contentDescription = null,
                painter = painterResource(R.drawable.ic_onboarding3),
                modifier = Modifier
                    .padding(vertical = 60.dp),
            )
            Image(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_kebab3),
            )
        }
        Button(
            onClick = { navController.navigate(Screen.OnBoard4.route) },
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, bottom = 30.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3469F9),
                contentColor = Color(0xFFFFFFFF)
            )
        ) {
            Text(
                text = stringResource(R.string.next),
                fontSize = 16.sp
            )
        }
    }
}
