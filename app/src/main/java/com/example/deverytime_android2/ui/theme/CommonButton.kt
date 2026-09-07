package com.example.deverytime_android2.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(25.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 22.dp,
                end = 22.dp,
                bottom = 30.dp
            ),
        contentPadding = PaddingValues(vertical = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainBlue,
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight.W700
        )
    }
}

