package com.example.deverytime_android2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.deverytime_android2.ui.theme.buttonGray
import com.example.deverytime_android2.ui.theme.mainBlue

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        NavigationBar(
            modifier =
                Modifier
                    .zIndex(0f)
                    .dropShadow(
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        shadow =
                            Shadow(
                                radius = 18.dp,
                                spread = 1.dp,
                                color = Color.Black.copy(alpha = 0.15f),
                                offset = DpOffset(x = 0.dp, (-7).dp),
                            ),
                    ).clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            Color(0xFFFFFFFF),
        ) {
            // TODO: 추후 홈 페이지 완성 이후 연동
            NavigationBarItem(
                modifier = Modifier.zIndex(1f).weight(1f),
                // selected는 임시 코드이므로 바꿔야함
                selected = currentRoute == Screen.Login.route,
                onClick = {
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = mainBlue,
                        selectedTextColor = mainBlue,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = buttonGray,
                        unselectedTextColor = buttonGray,
                    ),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.home_icon),
                        contentDescription = "홈",
                    )
                },
                label = {
                    Text(
                        text = "홈",
                        fontSize = 10.sp,
                    )
                },
            )

            NavigationBarItem(
                modifier = Modifier.zIndex(1f).weight(1f),
                selected = currentRoute == Screen.MyPage1.route,
                onClick = {
                    onNavigate(Screen.MyPage1.route)
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = mainBlue,
                        selectedTextColor = mainBlue,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = buttonGray,
                        unselectedTextColor = buttonGray,
                    ),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.mypage_icon),
                        contentDescription = "마이페이지",
                    )
                },
                label = {
                    Text(
                        text = "마이",
                        fontSize = 10.sp,
                    )
                },
            )
        }
        IconButton(
            onClick = {
                onNavigate(Screen.Login.route)
            },
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-36).dp)
                    .size(120.dp)
                    .zIndex(1f),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_button),
                    contentDescription = "로그인",
                    tint = Color(0xFF3469F9),
                )
                Icon(
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .padding(top = 2.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_plus),
                    contentDescription = "로그인",
                    tint = null,
                )
            }
        }
    }
}
