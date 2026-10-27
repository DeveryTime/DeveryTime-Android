package com.example.deverytime_android2

// 카테고리 ID·이름
data class PostCategory(
    val id: Int,
    val name: String,
)

// 임시 카테고리 목록
// 변경 시 ID·이름 함께 수정
internal val temporaryPostingCategories = listOf(
    PostCategory(4, "전공"),
    PostCategory(5, "일상"),
    PostCategory(6, "급식"),
    PostCategory(8, "분실물"),
    PostCategory(9, "동아리"),
)
