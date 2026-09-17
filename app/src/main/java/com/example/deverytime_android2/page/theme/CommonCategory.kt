package com.example.deverytime_android2.page.theme

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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonCategory(
    onClick: (String) -> Unit,
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
){
    val buttonColor = if (selected) lightBlue else Color.Transparent
    val borderColor = if (selected) lightBlue else Color.Gray
    val textColor = if (selected) White else Black

    OutlinedButton(
        modifier = modifier
            .height(23.dp)
            .padding(horizontal = 4.dp),
        onClick = { onClick(text) },
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
        )
    }
}
