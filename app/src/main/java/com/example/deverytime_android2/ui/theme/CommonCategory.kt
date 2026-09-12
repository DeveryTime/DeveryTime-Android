package com.example.deverytime_android2.ui.theme

import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.Main1Screen
import com.example.deverytime_android2.R
import com.example.deverytime_android2.pretendardVariable
import kotlinx.coroutines.selects.select

@Composable
fun CommonCategory(
    onclick: (String) -> Unit,
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
){
    val isDark = if (isSystemInDarkTheme()) White else Black
    val buttonColor = if (selected) lightBlue else Color.Transparent
    val borderColor = if (selected) lightBlue else Color.Gray
    val textColor = if (selected) White else isDark

    OutlinedButton(
        modifier = modifier
            .height(23.dp)
            .padding(horizontal = 4.dp),
        onClick = { onclick(text) },
        border = BorderStroke(1.dp, borderColor),
        contentPadding = PaddingValues(
            horizontal = 9.dp,
            vertical = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = textColor
        ),
    ) {
        Text(
            text = text,
            fontFamily = Pretendard,
            fontSize = 12.sp,
            color = textColor
        )
    }

}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun ScreenPreview() {
    DeveryTime_Android2Theme {
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 디자인 이미지를 반투명하게 배경에 깔기
            Image(
                painter = painterResource(id = R.drawable.main1),
                contentDescription = "디자인 미리보기",
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.3f),
                contentScale = ContentScale.FillWidth
            )
            // 실제 UI 겹치기
            Main1Screen(rememberNavController())
        }
    }
}
