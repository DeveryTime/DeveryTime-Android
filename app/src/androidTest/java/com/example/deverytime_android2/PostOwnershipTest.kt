package com.example.deverytime_android2

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.deverytime_android2.page.theme.DeveryTimeAndroid2Theme
import com.google.gson.Gson
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostOwnershipTest {
    @get:Rule
    val compose = createComposeRule()

    private fun showPost(writerId: Long?, currentUserId: Long?) {
        val json = """{"success":true,"data":{
            "id":5,"title":"제목","content":"내용","status":"PUBLISHED","viewCount":1,
            "writerId":$writerId,"writerNickname":"테스트유저","writerProfileImageUrl":null,
            "categoryName":"일상","imageUrls":[],"createdAt":"2026-08-25T19:51:41",
            "updatedAt":null,"likeCount":0,"liked":false
        }}"""
        val post = Gson().fromJson(json, PostDetailApiResponse::class.java).toPostDetail()
        compose.setContent {
            DeveryTimeAndroid2Theme {
                PostViewScreen(
                    navController = rememberNavController(),
                    post = post,
                    currentUserId = currentUserId,
                    commentListState = CommentListUiState.Success(emptyList()),
                )
            }
        }
        compose.onNodeWithContentDescription("케밥").performClick()
    }

    @Test
    fun ownerCanSeeEditAndDelete() {
        showPost(writerId = 4294967297L, currentUserId = 4294967297L)
        compose.onNodeWithText("수정하기").assertIsDisplayed()
        compose.onNodeWithText("삭제하기").assertIsDisplayed()
    }

    @Test
    fun anotherUserCannotSeeEditOrDelete() {
        showPost(writerId = 1L, currentUserId = 2L)
        compose.onNodeWithText("수정하기").assertDoesNotExist()
        compose.onNodeWithText("삭제하기").assertDoesNotExist()
        compose.onNodeWithText("신고하기").assertIsDisplayed()
    }

    @Test
    fun missingWriterIdDoesNotShowEditOrDelete() {
        showPost(writerId = null, currentUserId = 1L)
        compose.onNodeWithText("수정하기").assertDoesNotExist()
        compose.onNodeWithText("삭제하기").assertDoesNotExist()
    }

    @Test
    fun missingCurrentUserIdDoesNotShowEditOrDelete() {
        showPost(writerId = 1L, currentUserId = null)
        compose.onNodeWithText("수정하기").assertDoesNotExist()
        compose.onNodeWithText("삭제하기").assertDoesNotExist()
    }
}
