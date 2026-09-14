package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.Image
import com.example.deverytime_android2.ui.theme.CommonCategory
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.grayLineColor
import com.example.deverytime_android2.ui.theme.mainBlue
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.OutlinedRichTextEditor

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
)
@Composable
fun PostingScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var title by remember {
        mutableStateOf("")
    }
    val contentState = rememberRichTextState()
    val isKeyboardVisible = WindowInsets.isImeVisible

    var selectedCategory by remember { mutableStateOf("") }
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(start = 15.dp, top = 45.dp),
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier =
                        Modifier
                            .size(40.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x00FFFFFF)),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = stringResource(id = R.string.back_arrow),
                        contentScale = ContentScale.Fit,
                        modifier =
                            Modifier
                                .size(40.dp)
                                .align(Alignment.CenterVertically),
                    )
                }
                Text(
                    text = "글쓰기",
                    modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 3.dp),
                )
            }
            val categories =
                listOf(
                    "전공",
                    "일상",
                    "교과",
                    "급식",
                    "프로젝트",
                )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                categories.forEach { category ->
                    CommonCategory(
                        text = category,
                        selected = selectedCategory == category,
                        onclick = {
                            selectedCategory = it
                        },
                    )
                }
            }
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterHorizontally)
                        .height(50.dp),
                placeholder = {
                    Text(
                        text = "제목",
                        color = Color(0xFFBDBDBD),
                    )
                },
                shape = RoundedCornerShape(10.dp),
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedRichTextEditor(
                state = contentState,
                modifier =
                    Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.65f)
                        .align(Alignment.CenterHorizontally),
                placeholder = {
                    Text(
                        text = "본문",
                        color = Color(0xFFBDBDBD),
                    )
                },
                shape = RoundedCornerShape(10.dp),
            )
            Box(modifier = modifier.fillMaxSize()) {
                Button(
                    onClick = {
                        // TODO: 검증 후 백엔드로 전송(저장)
                        navController.navigate(Screen.MyPage1.route) {
                            popUpTo(Screen.MyPage1.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = mainBlue),
                    shape = RoundedCornerShape(23.dp),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 33.dp, start = 18.dp, end = 18.dp) // 33
                            .height(54.dp),
                ) {
                    Text(
                        fontFamily = pretendardVariable,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        text = "다음",
                    )
                }
            }
        }
        if (isKeyboardVisible) {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize provides 0.dp,
            ) {
                Row(
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .imePadding()
                            .background(Color.White)
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()

                                drawLine(
                                    color = grayLineColor,
                                    start = Offset(0f, strokeWidth / 2),
                                    end = Offset(size.width, strokeWidth / 2),
                                    strokeWidth = strokeWidth,
                                )
                                drawLine(
                                    color = grayLineColor,
                                    start = Offset(0f, size.height - strokeWidth / 2),
                                    end = Offset(size.width, size.height - strokeWidth / 2),
                                    strokeWidth = strokeWidth,
                                )
                            },
                ) {
                    IconButton(
                        onClick = {
                            contentState.toggleSpanStyle(
                                SpanStyle(fontWeight = FontWeight.Bold),
                            )
                        },
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_bold),
                            contentDescription = "Bold",
                        )
                    }
                    IconButton(
                        onClick = {
                            contentState.toggleSpanStyle(
                                SpanStyle(fontStyle = FontStyle.Italic),
                            )
                        },
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_italic),
                            contentDescription = "Italic",
                        )
                    }
                    IconButton(
                        onClick = {
                            contentState.toggleSpanStyle(
                                SpanStyle(textDecoration = TextDecoration.Underline),
                            )
                        },
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_underline),
                            contentDescription = "Underline",
                        )
                    }
                    IconButton(
                        onClick = {
                            contentState.toggleSpanStyle(
                                SpanStyle(textDecoration = TextDecoration.LineThrough),
                            )
                        },
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_midline),
                            contentDescription = "Line Through",
                        )
                    }
                    IconButton(
                        onClick = {
                            contentState.toggleParagraphStyle(
                                ParagraphStyle(textAlign = TextAlign.Start),
                            )
                        },
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_align_start),
                            contentDescription = "Align Left",
                        )
                    }
                    IconButton(
                        onClick = {
                            contentState.toggleParagraphStyle(
                                ParagraphStyle(textAlign = TextAlign.Center),
                            )
                        },
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_align_center),
                            contentDescription = "Align Center",
                        )
                    }
                    IconButton(
                        onClick = {
                            contentState.toggleParagraphStyle(
                                ParagraphStyle(textAlign = TextAlign.End),
                            )
                        },
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_align_end),
                            contentDescription = "Align Right",
                        )
                    }
                    IconButton(
                        onClick = {
                            val selection = contentState.selection

                            if (!selection.collapsed) {
                                val selectedText =
                                    contentState.annotatedString.text.substring(
                                        selection.min,
                                        selection.max,
                                    )

                                val url =
                                    if (selectedText.startsWith("http")) {
                                        selectedText
                                    } else {
                                        "https://$selectedText"
                                    }

                                contentState.addLinkToSelection(url)
                            }
                        },
                        enabled = !contentState.selection.collapsed,
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_link),
                            contentDescription = "Add Link",
                        )
                    }
                    IconButton(
                        onClick = { contentState.history.undo() },
                        enabled = contentState.history.canUndo,
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_undo_outline),
                            contentDescription = "Undo",
                        )
                    }
                    IconButton(
                        onClick = { contentState.history.redo() },
                        enabled = contentState.history.canRedo,
                    ) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_do_outline),
                            contentDescription = "Redo",
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4", showSystemUi = true)
@Composable
fun GreetingPreview() {
    DeveryTime_Android2Theme {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.posting),
                contentDescription = "디자인 미리보기",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .alpha(0.3f),
                contentScale = ContentScale.Fit,
            )
        }
        PostingScreen(navController = NavHostController(LocalContext.current))
    }
}
