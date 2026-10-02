package com.example.deverytime_android2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.page.theme.BottomNavigationBar
import com.example.deverytime_android2.page.theme.CommonSearchBar
import com.example.deverytime_android2.page.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.page.theme.Style
import com.example.deverytime_android2.page.theme.buttonGray
import com.example.deverytime_android2.page.theme.grayLineColor
import com.example.deverytime_android2.page.theme.nonprofile
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState



//해당 부분은 GPT 5.6 SOL을 사용하여 구성한 부분입니다.

/**
 * 검색 결과에 표시할 사용자 UI 모델입니다.
 *
 * 현재는 API가 연결되지 않아 화면에 필요한 최소 정보만 가지고 있습니다.
 * 추후 사용자 검색 API가 연결되면 서버 응답 DTO를 이 형태로 변환하거나,
 * 서버 모델에 맞게 필드를 추가하면 됩니다.
 *
 * 예상 추가 필드: 사용자 ID, 프로필 이미지 URL, 학번 등
 */
private data class SearchUser(
    val name: String,
    val introduction: String,
)

/**
 * 검색 결과 목록에서 사용하는 게시글 UI 모델입니다.
 *
 * 기존 [Post] 전체를 직접 사용하지 않고 검색 결과에 필요한 값만 분리했습니다.
 * API 연결 후에는 게시글 검색 응답을 이 모델로 변환하면 UI 코드는 그대로 유지할 수 있습니다.
 */
private data class SearchPost(
    val id: Int,
    val title: String,
    val content: String,
    val time: String,
)

// TODO: 사용자 검색 API 연동 후 서버 응답으로 교체합니다.
// 디자인 확인을 위해 main_search.png에 표시된 사용자 정보를 임시로 사용하고 있습니다.
private val mockSearchUsers =
    listOf(
        SearchUser(
            name = "파이썬 마스터",
            introduction = "안녕하세요 반가워용",
        ),
    )

// TODO: 게시글 검색 API 연동 후 서버 응답으로 교체합니다.
// 앞의 두 항목은 디자인 시안 재현용이고, dummyPosts는 다른 검색어도 시험할 수 있도록 합쳤습니다.
private val mockSearchPosts =
    listOf(
        SearchPost(
            id = 0,
            title = "파이썬 마스터 되는 법",
            content = "파이썬 공부 방법을 공유합니다.",
            time = "2026-08-29T12:00:00",
        ),
        SearchPost(
            id = 1,
            title = "파이썬 마스터 되는 법",
            content = "기초부터 공부하는 방법입니다.",
            time = "2026-08-29T12:00:00",
        ),
    ) +
        dummyPosts.map { post ->
            SearchPost(
                id = post.id,
                title = post.title,
                content = post.content,
                time = post.time,
            )
        }

// Main1~4 화면에서 입력한 검색어를 SavedStateHandle을 통해 전달할 때 사용하는 공통 키입니다.
// 문자열을 여러 파일에 직접 작성하지 않고 이 상수를 사용해야 오타로 인한 전달 실패를 막을 수 있습니다.
const val SEARCH_QUERY_KEY = "searchQuery"

/**
 * 검색 결과 화면의 진입점입니다.
 *
 * 화면은 크게 두 겹으로 구성됩니다.
 * 1. 아래쪽의 최신 게시글 영역
 * 2. 그 위에 떠 있는 검색 결과 패널
 *
 * [navigator]는 이전 화면의 검색어를 가져오고 게시글 상세 화면으로 이동할 때 사용합니다.
 */
@Composable
fun SearchScreen(
    navigator: NavHostController,
    modifier: Modifier = Modifier,
) {
    val searchViewModel: SearchViewModel = viewModel()

    val searchResult by searchViewModel.searchResult.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()
    val errorMessage by searchViewModel.errorMessage.collectAsState()
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

    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            searchViewModel.search(initialQuery)
        }
    }

    // 검색 아이콘 또는 키보드의 검색 버튼을 눌렀을 때 키보드를 닫는 데 사용합니다.
    val focusManager = LocalFocusManager.current

    // 앞뒤 공백을 제거한 문자열을 실제 검색 조건으로 사용합니다.
    val normalizedQuery = query.trim()

    val apiPosts =
        searchResult?.data?.content.orEmpty().map { post ->
            SearchPost(
                id = post.id,
                title = post.title,
                content = post.categoryName,
                time = post.createdAt
            )
        }

    // 사용자 이름 또는 자기소개에 검색어가 포함되는지 확인합니다.
    // 검색어가 비어 있으면 디자인 확인을 위해 기본 임시 데이터를 노출합니다.
    // 디자인상 사용자 결과는 한 명만 표시되므로 마지막에 take(1)을 적용했습니다.
    val filteredUsers =
        remember(normalizedQuery) {
            if (normalizedQuery.isBlank()) {
                mockSearchUsers
            } else {
                mockSearchUsers.filter { user ->
                    user.name.contains(normalizedQuery, ignoreCase = true) ||
                        user.introduction.contains(normalizedQuery, ignoreCase = true)
                }
            }
        }.take(1)

    // 게시글 제목 또는 본문에 검색어가 포함되는지 확인합니다.
    // 검색어가 비어 있으면 임시 게시글 전체를 후보로 사용하고, 화면에는 최대 두 개만 표시합니다.


    // Box를 사용해 최신 게시글 영역 위에 검색 결과 패널을 겹쳐 배치합니다.
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        // 배경에 놓이는 홈 화면의 최신 게시글 영역입니다.
        LatestPostSection(modifier = Modifier.fillMaxSize())

        // 검색창과 검색 결과가 들어 있는 흰색 카드입니다.
        // zIndex를 지정해 LatestPostSection보다 항상 위에 그려지도록 합니다.
        SearchResultPanel(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                focusManager.clearFocus()

                if (normalizedQuery.isNotBlank()) {
                    searchViewModel.search(normalizedQuery)
                }
            },
            users = filteredUsers,
            posts = apiPosts,

            // 검색 결과의 게시글을 누르면 기존 게시글 상세 라우트로 이동합니다.
            onPostClick = { postId ->
                navigator.navigate("postView/$postId")
            },
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

/**
 * 검색창, 사용자 결과, 게시글 결과를 하나의 카드로 묶는 컴포넌트입니다.
 *
 * 검색 로직은 상위 [SearchScreen]이 담당하고 이 컴포넌트는 전달받은 결과를 표시합니다.
 * 이렇게 분리하면 API 연결 후에도 데이터 요청 부분과 UI 부분을 독립적으로 변경할 수 있습니다.
 */
@Composable
private fun SearchResultPanel(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    users: List<SearchUser>,
    posts: List<SearchPost>,
    onPostClick: (Int) -> Unit,
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

        // 사용자 검색 결과가 있을 때만 사용자 섹션을 표시합니다.
        if (users.isNotEmpty()) {
            Text(
                text = "사용자",
                fontFamily = pretendardVariable,
                fontSize = 14.sp,
                color = buttonGray,
                modifier = Modifier.padding(start = 13.dp, top = 13.dp),
            )
            users.forEach { user ->
                SearchUserResultItem(
                    user = user,
                    modifier = Modifier.padding(horizontal = 13.dp),
                )
            }
        }

        // 게시글 검색 결과가 있을 때만 게시글 섹션을 표시합니다.
        if (posts.isNotEmpty()) {
            Text(
                text = "게시글",
                fontFamily = pretendardVariable,
                fontSize = 14.sp,
                color = buttonGray,
                modifier = Modifier.padding(start = 13.dp, top = 9.dp, bottom = 3.dp),
            )
            posts.forEach { post ->
                SearchPostResultItem(
                    post = post,
                    onClick = { onPostClick(post.id) },
                )
            }
        }

        // 사용자와 게시글 검색 결과가 모두 없을 때 카드 중앙에 빈 상태를 표시합니다.
        if (users.isEmpty() && posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "검색 결과가 없습니다",
                    fontFamily = pretendardVariable,
                    fontSize = 14.sp,
                    color = buttonGray,
                )
            }
        }
    }
}

/** 검색 결과의 사용자 한 명을 표시하는 행입니다. */
@Composable
private fun SearchUserResultItem(
    user: SearchUser,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(45.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // API 연결 후 프로필 이미지 URL이 추가되면 AsyncImage 등으로 교체할 영역입니다.
        Box(
            modifier =
                Modifier
                    .size(34.dp)
                    .background(nonprofile, CircleShape),
        )
        Column(modifier = Modifier.padding(start = 7.dp)) {
            Text(
                text = user.name,
                fontFamily = pretendardVariable,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = user.introduction,
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
    post: SearchPost,
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
                text = post.title,
                fontFamily = pretendardVariable,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                // 검색 시안은 연도를 네 자리로 표시하므로 yyyy.MM.dd 형식을 사용합니다.
                text = formatTime(post.time, "yyyy.MM.dd"),
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
 * 현재 API가 연결되지 않았기 때문에 시안과 동일하게 빈 상태만 표시합니다.
 *
 * 추후 최신 게시글 API가 연결되면 Box의 빈 상태 Text를 LazyColumn 또는 게시글 목록으로 교체하면 됩니다.
 */
@Composable
private fun LatestPostSection(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .padding(top = 394.dp),
    ) {
        Text(
            text = "최신순 >",
            style = Style.SubTitle,
            modifier = Modifier.padding(start = 14.dp),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            contentAlignment = Alignment.TopCenter,
        ) {
            Text(
                text = "작성된 게시물이 없기",
                fontFamily = pretendardVariable,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 106.dp),
            )
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
    DeveryTime_Android2Theme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = Screen.Search.route,
                    onNavigate = {},
                )
            },
        ) { innerPadding ->
            SearchScreen(
                navigator = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
