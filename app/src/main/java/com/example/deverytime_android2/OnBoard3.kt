package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.deverytime_android2.ui.theme.CommonButton
import com.example.deverytime_android2.ui.theme.Style

@Composable
fun OnBoard3Screen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(108.dp))
            Text(
                text = stringResource(R.string.onboard3_1),
                style = Style.Title,
            )
            Text(
                text = stringResource(R.string.onboard3_2),
                style = Style.Title,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(93.dp))
            Image(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_onboarding3),
            )
            Spacer(Modifier.height(60.dp))
            Image(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_kebab3),
            )
            Spacer(Modifier.height(80.dp))
        }
        CommonButton(
            text = stringResource(R.string.next),
            onClick = { navController.navigate(Screen.OnBoard4.route) }
        )
    }
}
