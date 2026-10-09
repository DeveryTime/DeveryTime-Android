package com.example.deverytime_android2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

internal const val POST_UPDATED_KEY = "post-updated"

// 게시글 ID 전달·상세 조회
@Composable
fun PostDetailScreen(
    navController: NavHostController,
    postId: Long,
    model: PostViewModel = viewModel(),
    isEditing: Boolean = false,
    currentUserId: Long? = null,
) {
    val state by model.state.collectAsState()
    val deleteState by model.deleteState.collectAsState()
    val entry = remember(navController) { requireNotNull(navController.currentBackStackEntry) }
    val postUpdated by entry.savedStateHandle.getStateFlow(POST_UPDATED_KEY, false).collectAsState()
    LaunchedEffect(postId) { model.loadPost(postId) }
    // 삭제 완료 후 이전 목록 갱신·뒤로 이동
    LaunchedEffect(deleteState) {
        if (deleteState is DeletePostUiState.Success) {
            navController.previousBackStackEntry?.savedStateHandle?.set(POST_UPDATED_KEY, true)
            navController.popBackStack()
        }
    }
    // 수정 완료 후 상세·이전 목록 재조회
    LaunchedEffect(postUpdated) {
        if (postUpdated) {
            model.loadPost(postId)
            navController.previousBackStackEntry?.savedStateHandle?.set(POST_UPDATED_KEY, true)
            entry.savedStateHandle[POST_UPDATED_KEY] = false
        }
    }

    val currentState = state
    if (currentState is PostDetailUiState.Success) {
        if (isEditing) {
            PostingScreen(
                navController = navController,
                editingPost = currentState.post,
                currentUserId = currentUserId,
            )
        } else {
            PostViewScreen(
                navController = navController,
                post = currentState.post,
                currentUserId = currentUserId,
                deleteState = deleteState,
                onDelete = { model.deletePost(postId) },
                onDeleteDismiss = model::resetDeleteState,
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            PostBackButton(navController)
            Box(modifier = Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
                when (currentState) {
                    PostDetailUiState.Loading -> CircularProgressIndicator()
                    is PostDetailUiState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(currentState.message, modifier = Modifier.padding(horizontal = 24.dp))
                        TextButton(onClick = { model.loadPost(postId) }) { Text("다시 시도") }
                    }
                }
            }
        }
    }
}

// 기존 상세 화면의 뒤로가기 버튼
@Composable
internal fun PostBackButton(navController: NavHostController) {
    Row(modifier = Modifier.padding(start = 15.dp, top = 45.dp)) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.size(40.dp),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x00FFFFFF)),
        ) {
            Image(
                painter = painterResource(R.drawable.back_arrow),
                contentDescription = stringResource(R.string.back_arrow),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp).align(Alignment.CenterVertically),
            )
        }
    }
}
