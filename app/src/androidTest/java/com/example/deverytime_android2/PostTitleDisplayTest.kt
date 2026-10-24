package com.example.deverytime_android2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import com.example.deverytime_android2.page.theme.DeveryTimeAndroid2Theme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PostTitleDisplayTest {
    @get:Rule
    val compose = createComposeRule()

    private fun showTitle(title: String) {
        compose.setContent {
            DeveryTimeAndroid2Theme {
                Column(Modifier.width(360.dp)) {
                    PostItem(title = title, time = "2026-10-23T12:00:00", like = 1)
                }
            }
        }
    }

    @Test
    fun leadingLineBreakDoesNotHideOtherwiseShortTitle() {
        showTitle("\n첫 줄 제목\r\n다음 줄")
        val results = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText("첫 줄 제목 다음 줄", useUnmergedTree = true)
            .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(results) }
        val layout = results.single()
        assertFalse(layout.isLineEllipsized(0))
        assertTrue(layout.getLineEnd(0, visibleEnd = true) > 0)
    }

    @Test
    fun longTitleKeepsVisiblePrefixBeforeEllipsis() {
        val title = "긴 게시글 제목입니다 ".repeat(40).trim()
        showTitle("\n$title")
        val results = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText(title, useUnmergedTree = true)
            .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(results) }
        val layout = results.single()
        assertTrue(layout.isLineEllipsized(0))
        assertTrue(layout.getLineEnd(0, visibleEnd = true) > 0)
    }
}
