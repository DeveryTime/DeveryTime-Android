package com.example.deverytime_android2

// 카테고리 ID·이름
data class PostCategory(
    val id: Int,
    val name: String,
)

// 임시 카테고리 목록
// 변경 시 ID·이름 함께 수정
internal val temporaryPostingCategories = listOf(
    PostCategory(1, "전공"),
    PostCategory(2, "일상"),
    PostCategory(3, "교과"),
    PostCategory(4, "급식"),
    PostCategory(5, "프로젝트"),
    PostCategory(6, "기숙사"),
    PostCategory(7, "분실물"),
)
