package com.example.deverytime_android2

import com.mohamedrejeb.richeditor.model.RichTextState

// 본문과 서식을 함께 JSON의 content 문자열로 전송
internal fun RichTextState.toPostMarkdown(): String =
    if (annotatedString.text.isBlank()) "" else toMarkdown()
