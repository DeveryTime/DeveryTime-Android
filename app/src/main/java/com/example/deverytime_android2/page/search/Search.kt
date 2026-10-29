package com.example.deverytime_android2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.deverytime_android2.page.theme.BottomNavigationBar
import com.example.deverytime_android2.page.theme.CommonSearchBar
import com.example.deverytime_android2.page.theme.DeveryTimeAndroid2Theme
import com.example.deverytime_android2.page.theme.Style
import com.example.deverytime_android2.page.theme.buttonGray
import com.example.deverytime_android2.page.theme.grayLineColor
import com.example.deverytime_android2.page.theme.nonprofile



//해당 부분은 GPT 5.6 SOL을 사용하여 구성한 부분입니다.

// Main1~4 화면에서 입력한 검색어를 SavedStateHandle을 통해 전달할 때 사용하는 공통 키입니다.
// 문자열을 여러 파일에 직접 작성하지 않고 이 상수를 사용해야 오타로 인한 전달 실패를 막을 수 있습니다.
const val SEARCH_QUERY_KEY = "searchQuery"

// 검색 화면 UI·이전 화면의 검색어 표시
@Composable
fun SearchScreen(
    navigator: NavHostController,
    modifier: Modifier = Modifier,
    model: SearchViewModel = viewModel(),
) {
    // 검색 버튼을 누른 이전 화면(Main1~4)의 SavedStateHandle에서 검색어를 읽습니다.
    // Preview처럼 이전 화면이 없는 경우에는 빈 문자열을 사용합니다.
    val initialQuery =
        remember(navigator) {
            navigator.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>(SEARCH_QUERY_KEY)
                .orEmpty()
        }

    // rememberSaveable을 사용하므로 화면 재구성뿐 아니라 구성 변경이 발생해도 입력값이 유지됩니다.
    var query by rememberSaveable { mutableStateOf(initialQuery) }

    // 검색 아이콘 또는 키보드의 검색 버튼을 눌렀을 때 키보드를 닫는 데 사용합니다.
    val focusManager = LocalFocusManager.current
    val state by model.state.collectAsState()

    // 메인 화면에서 전달받은 검색어로 첫 진입 시 검색
    LaunchedEffect(Unit) {
        if (!state.hasSearched && initialQuery.isNotBlank()) model.search(initialQuery)
    }

    SearchContent(
        query = query,
        onQueryChange = { query = it },
        onSearch = {
            focusManager.clearFocus()
            model.search(query)
        },
        state = state,
        onPostClick = { id -> navigator.navigate("postView/$id") },
        onLatestClick = { navigator.navigate(Screen.SearchLatest.route) },
        onRetry = model::retry,
        modifier = modifier,
    )
}

@Composable
private fun SearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    state: SearchUiState,
    onPostClick: (Long) -> Unit,
    onLatestClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Box를 사용해 최신 게시글 영역 위에 검색 결과 패널을 겹쳐 배치합니다.
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        // 검색된 게시글을 최신순으로 보여주는 하단 영역입니다.
        LatestPostSection(
            state = state,
            onPostClick = onPostClick,
            onTitleClick = onLatestClick,
            modifier = Modifier.fillMaxSize(),
        )

        // 검색창과 사용자 검색 결과가 들어 있는 흰색 카드입니다.
        // zIndex를 지정해 LatestPostSection보다 항상 위에 그려지도록 합니다.
        SearchResultPanel(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            state = state,
            onPostClick = onPostClick,
            onRetry = onRetry,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(start = 7.dp, top = 40.dp, end = 7.dp)
                    .fillMaxWidth()
                    .height(304.dp)
                    .zIndex(1f),
        )
    }
}

// 검색창·사용자 결과 카드 (사용자마다 그 사용자가 쓴 글을 바로 아래에 표시)
@Composable
private fun SearchResultPanel(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    state: SearchUiState,
    onPostClick: (Long) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                // main_search.png처럼 카드 아래쪽에 그림자가 보이도록 설정합니다.
                .dropShadow(
                    shape = RoundedCornerShape(12.dp),
                    shadow =
                        Shadow(
                            radius = 8.dp,
                            spread = 1.dp,
                            color = Color.Black.copy(alpha = 0.16f),
                            offset = DpOffset(x = 0.dp, y = 4.dp),
                        ),
                )
                .background(Color.White, RoundedCornerShape(12.dp)
                ),
        verticalArrangement = Arrangement.Top,
    ) {
        // 프로젝트의 공용 검색창을 재사용합니다.
        // 검색 아이콘과 키보드 검색 액션 모두 onSearch를 호출합니다.
        CommonSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            modifier = Modifier.padding(start = 7.dp, top = 5.dp, end = 7.dp),
        )

        // 결과가 많아도 기존 카드 안에서 스크롤로 확인
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            if (state.users.isNotEmpty()) {
                item(key = "users-title") {
                    Text(
                        text = "사용자",
                        fontFamily = pretendardVariable,
                        fontSize = 14.sp,
                        color = buttonGray,
                        modifier = Modifier.padding(start = 13.dp, top = 13.dp),
                    )
                }
                state.users.forEach { user ->
                    item(key = "user-${user.id}") {
                        SearchUserResultItem(
                            user = user,
                            modifier = Modifier.padding(horizontal = 13.dp),
                        )
                    }
                    // 해당 사용자가 작성한 최신 글을 사용자 바로 아래에 표시
                    val userPosts = state.postsWrittenBy(user.id)
                    items(userPosts, key = { "user-${user.id}-post-${it.id}" }) { post ->
                        SearchPostResultItem(post = post, onClick = { onPostClick(post.id) })
                    }
                    if (userPosts.isEmpty() && (state.userPostsLoading || state.userPosts != null)) {
                        item(key = "user-${user.id}-posts-status") {
                            Text(
                                text = if (state.userPostsLoading) "작성한 글을 불러오는 중입니다" else "작성한 글이 없습니다",
                                fontFamily = pretendardVariable,
                                fontSize = 12.sp,
                                color = buttonGray,
                                modifier = Modifier.padding(start = 13.dp, bottom = 6.dp),
                            )
                        }
                    }
                }
            }

            item(key = "search-status") {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when {
                        state.loading -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        state.error != null -> {
                            Text(state.error, fontFamily = pretendardVariable, fontSize = 14.sp)
                            if (state.hasSearched) {
                                TextButton(onClick = onRetry) { Text("다시 시도") }
                            }
                        }
                        !state.hasSearched -> Text(
                            "검색어를 입력해 주세요.",
                            fontFamily = pretendardVariable,
                            fontSize = 14.sp,
                            color = buttonGray,
                        )
                        state.users.isEmpty() -> Text(
                            "검색된 사용자가 없습니다",
                            fontFamily = pretendardVariable,
                            fontSize = 14.sp,
                            color = buttonGray,
                        )
                    }
                }
            }
        }
    }
}

/** 검색 결과의 사용자 한 명을 표시하는 행입니다. */
@Composable
private fun SearchUserResultItem(
    user: SearchUserResult,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(45.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 프로필 사진이 없거나 불러오지 못하면 회색 원 배경만 보임
        AsyncImage(
            model = user.profileImageUrl?.takeIf { it.isNotBlank() },
            contentDescription = "${user.displayName} 프로필 사진",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(nonprofile),
        )
        Column(modifier = Modifier.padding(start = 7.dp)) {
            Text(
                text = user.displayName,
                fontFamily = pretendardVariable,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = user.username.orEmpty(),
                fontFamily = pretendardVariable,
                fontSize = 12.sp,
                color = buttonGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * 검색 결과의 게시글 한 개를 표시하는 행입니다.
 * 기존 PostItem은 좋아요 수와 다른 배치를 사용하므로 검색 화면 전용 항목으로 분리했습니다.
 */
@Composable
private fun SearchPostResultItem(
    post: PostListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = singleLinePostTitle(post.title),
                fontFamily = pretendardVariable,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                // 검색 시안은 연도를 네 자리로 표시하므로 yyyy.MM.dd 형식을 사용합니다.
                text = formatTime(post.createdAt, "yyyy.MM.dd"),
                fontFamily = pretendardVariable,
                fontSize = 12.sp,
                color = buttonGray,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = grayLineColor,
        )
    }
}

/**
 * 검색 결과 패널 아래에 보이는 최신 게시글 영역입니다.
 * 통합 검색 API가 createdAt,desc로 정렬해 준 게시글 결과를 메인 홈의 최신순처럼 4개까지 표시하고,
 * 제목을 누르면 전체 검색 결과 목록(SearchLatestScreen)으로 이동합니다.
 */
@Composable
private fun LatestPostSection(
    state: SearchUiState,
    onPostClick: (Long) -> Unit,
    onTitleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(top = 394.dp),
    ) {
        Text(
            text = "최신순 >",
            style = Style.SubTitle,
            modifier =
                Modifier
                    .clickable(onClick = onTitleClick)
                    .padding(start = 15.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (state.posts.isEmpty()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                // 첫 페이지를 불러오는 중에는 상단 카드의 로딩 표시만 보여줌
                if (!state.loading) {
                    Text(
                        text = "작성된 게시물이 없습니다",
                        fontFamily = pretendardVariable,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 106.dp),
                    )
                }
            }
        } else {
            state.posts.take(4).forEachIndexed { index, post ->
                PostItem(
                    title = post.title,
                    time = post.createdAt,
                    like = post.likeCount,
                    showTopBorder = (index == 0),
                    onClick = { onPostClick(post.id) },
                )
            }
        }
    }
}

/**
 * 검색 화면의 "최신순 >"을 눌렀을 때 이동하는 전체 검색 게시글 목록입니다.
 * 메인의 최신순 전체 화면(Main3)과 같은 형태로, 스크롤 끝에서 다음 페이지를 이어서 불러옵니다.
 * 검색 화면의 SearchViewModel을 함께 사용하므로 이미 불러온 결과를 다시 요청하지 않습니다.
 */
@Composable
fun SearchLatestScreen(
    navigator: NavHostController,
    backStackEntry: NavBackStackEntry,
) {
    // 현재 화면의 entry를 키로 써야 검색 화면 entry를 매번 다시 찾지 않음
    val searchEntry = remember(backStackEntry) { navigator.getBackStackEntry(Screen.Search.route) }
    val model: SearchViewModel = viewModel(searchEntry)
    val state by model.state.collectAsState()
    val listState = rememberLazyListState()
    // 스크롤 하단 자동 로딩
    ObserveListEnd(listState, state.page, state.loading, state.error, state.hasNextPage, model::loadNext)

    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = { navigator.popBackStack() },
            modifier =
                Modifier
                    .padding(start = 8.dp, top = 48.dp)
                    .size(32.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = stringResource(R.string.back_arrow),
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = "최신순",
            style = Style.SubTitle,
        )
        LazyColumn(
            state = listState,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            itemsIndexed(items = state.posts, key = { _, post -> post.id }) { index, post ->
                PostItem(
                    title = post.title,
                    time = post.createdAt,
                    like = post.likeCount,
                    showTopBorder = (index == 0),
                    onClick = { navigator.navigate("postView/${post.id}") },
                )
            }
            item { SearchPostListStatus(state, model::retry) }
        }
    }
}

// 메인 목록의 PostListStatus와 같은 로딩·오류·빈 목록 표시
@Composable
private fun SearchPostListStatus(state: SearchUiState, retry: () -> Unit) {
    if (!state.loading && state.error == null && state.posts.isNotEmpty()) return
    Column(modifier = Modifier.padding(horizontal = 21.dp, vertical = 12.dp)) {
        when {
            state.loading -> CircularProgressIndicator()
            state.error != null -> Text(
                text = "${state.error}\n눌러서 다시 시도",
                modifier = Modifier.clickable(onClick = retry),
            )
            state.posts.isEmpty() -> Text("게시글이 없습니다.")
        }
    }
}

// 393x852 크기의 디자인 시안과 비교하기 위한 전체 화면 Preview입니다.
// 실제 앱에서는 MainActivity의 Scaffold가 하단 내비게이션을 제공하지만,
// Preview에서는 SearchScreen만 렌더링되므로 Scaffold와 BottomNavigationBar를 함께 구성했습니다.
@Preview(
    showBackground = true,
    device = "id:pixel_4",
)
@Composable
private fun SearchScreenPreview() {
    DeveryTimeAndroid2Theme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = Screen.Search.route,
                    onNavigate = {},
                )
            },
        ) { innerPadding ->
            SearchContent(
                query = "대회",
                onQueryChange = {},
                onSearch = {},
                state = SearchUiState(
                    keyword = "대회",
                    users = listOf(SearchUserResult(5L, "최지민", "지민")),
                    posts = listOf(
                        PostListItem(13L, "지민의 대회 후기", "일상", "2026-08-26T10:00:00", writerId = 5L),
                        PostListItem(12L, "대회 안내", "일상", "2026-08-25T19:41:54", writerId = 1L),
                    ),
                    hasSearched = true,
                    page = 0,
                    totalPages = 1,
                ),
                onPostClick = {},
                onLatestClick = {},
                onRetry = {},
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
