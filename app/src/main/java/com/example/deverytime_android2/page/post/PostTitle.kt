package com.example.deverytime_android2

private val titleLineBreaks = Regex("[\\r\\n\\t\\u2028\\u2029]+")

// 한 줄 목록에서 선행 줄바꿈 때문에 제목 대신 말줄임표만 보이지 않도록 정리
internal fun singleLinePostTitle(title: String): String = title.replace(titleLineBreaks, " ").trim()
