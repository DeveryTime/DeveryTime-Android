package com.example.deverytime_android2

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeveryTime_Android2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        schoolmail = "이메일",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun Greeting(
    schoolmail: String,
    modifier: Modifier = Modifier
){
Column (modifier = modifier.padding(16.dp,end = 16.dp, top = 220.dp).fillMaxWidth()) {

    var schoolmailreciver by remember { mutableStateOf("") }

    Text(
        text = "이메일",
        color = Color(0xFF999999)
        )
    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            // 1. 입력한 글자 색상
            focusedTextColor = Color.Black,     // 클릭(포커스)했을 때 글자 색
            unfocusedTextColor = Color.Black,   // 기본 상태 글자 색

            // 2. 안내글자(Placeholder) 색상
            focusedPlaceholderColor = Color.Transparent, // 클릭하면 안내글자 숨기기
            unfocusedPlaceholderColor = Color(0xFF999999), // 기본 상태 안내글자 색

            // 3. 에러 났을 때 색상
            errorBorderColor = Color.Red,
        ),
        placeholder = { Text(text = "이메일")},
        value = schoolmailreciver,
        onValueChange = { schoolmailreciver = it },
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun GreetingPreview() {
    DeveryTime_Android2Theme {
        Greeting("")
    }
}