package com.example.deverytime_android2

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.deverytime_android2.page.theme.DeveryTimeAndroid2Theme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentFeedbackTest {
    @get:Rule
    val compose = createComposeRule()

    private val post = PostDetailResponse(
        id = 1L,
        title = "테스트 게시글",
        content = "테스트 내용",
        status = "PUBLISHED",
        viewCount = 0,
        writer = PostWriter(1L, "작성자", null),
        category = PostDetailCategory(1L, "일상"),
        images = emptyList(),
        createdAt = "2026-10-20T12:00:00",
        updatedAt = null,
    )

    @Test
    fun nextCommentCanBeSentWhileSuccessSnackbarIsVisible() {
        val state = mutableStateOf<CreateCommentUiState>(CreateCommentUiState.Idle)
        val submitted = mutableListOf<String>()
        var handled = 0
        compose.setContent {
            DeveryTimeAndroid2Theme {
                PostViewScreen(
                    navController = rememberNavController(),
                    post = post,
                    commentState = state.value,
                    commentListState = CommentListUiState.Success(emptyList()),
                    onCommentHandled = {
                        handled++
                        state.value = CreateCommentUiState.Idle
                    },
                    onCreateComment = { submitted.add(it) },
                )
            }
        }

        compose.runOnIdle {
            state.value = CreateCommentUiState.Success(
                CommentData(1L, 1L, 5L, "첫 댓글", "2026-10-20T12:00:00"),
                "댓글이 등록되었습니다.",
            )
        }
        compose.onNodeWithText("댓글이 등록되었습니다.").assertIsDisplayed()
        compose.onNode(hasSetTextAction()).assertIsEnabled().performTextInput("다음 댓글")
        compose.onNodeWithContentDescription("댓글 전송").performClick()
        compose.runOnIdle {
            assertEquals(1, handled)
            assertEquals(listOf("다음 댓글"), submitted)
        }
    }

    @Test
    fun deleteSuccessReleasesInputBeforeSnackbarDismisses() {
        val state = mutableStateOf<DeleteCommentUiState>(DeleteCommentUiState.Idle)
        var handled = 0
        compose.setContent {
            DeveryTimeAndroid2Theme {
                PostViewScreen(
                    navController = rememberNavController(),
                    post = post,
                    commentDeleteState = state.value,
                    commentListState = CommentListUiState.Success(emptyList()),
                    onCommentDeleteHandled = {
                        handled++
                        state.value = DeleteCommentUiState.Idle
                    },
                )
            }
        }

        compose.runOnIdle { state.value = DeleteCommentUiState.Success("댓글이 삭제되었습니다.") }
        compose.onNodeWithText("댓글이 삭제되었습니다.").assertIsDisplayed()
        compose.onNode(hasSetTextAction()).assertIsEnabled()
        compose.runOnIdle { assertEquals(1, handled) }
    }
}
