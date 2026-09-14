package com.example.deverytime_android2.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonCategory(
    onclick: (String) -> Unit,
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    var buttonColor = if (selected) lightBlue else Color.Gray
    var borderColor = if (selected) lightBlue else Color.Gray
    OutlinedButton(
        modifier =
            modifier
                .height(23.dp)
                .padding(horizontal = 4.dp),
        onClick = {
            buttonColor = if (buttonColor == lightBlue) Color.Gray else lightBlue
            borderColor = if (borderColor == lightBlue) Color.Gray else lightBlue
            onclick(text)
        },
        border = BorderStroke(1.dp, borderColor),
        contentPadding =
            PaddingValues(
                horizontal = 9.dp,
                vertical = 0.dp,
            ),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = if (selected) lightBlue else Color.Transparent,
            ),
    ) {
        Text(
            text = text,
            fontFamily = Pretendard,
            fontSize = 12.sp,
            color = Color.Black,
        )
    }
}

// @Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
// @Composable
// fun ScreenPreview() {
//    DeveryTime_Android2Theme {
//        val scrollState = rememberScrollState()
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//        ) {
//            // 디자인 이미지를 반투명하게 배경에 깔기
//            Image(
//                painter = painterResource(id = R.drawable.main1),
//                contentDescription = "디자인 미리보기",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .alpha(0.3f),
//                contentScale = ContentScale.FillWidth
//            )
//            // 실제 UI 겹치기
//            Main1Screen(rememberNavController())
//        }
//    }
// }
