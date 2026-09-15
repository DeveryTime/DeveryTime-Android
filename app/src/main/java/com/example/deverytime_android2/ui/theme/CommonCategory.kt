package com.example.deverytime_android2.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
