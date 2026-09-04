package com.example.deverytime_android2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme
import com.example.deverytime_android2.ui.theme.buttonGray
import com.example.deverytime_android2.ui.theme.mainBlue

sealed class Screen(
    val route: String,
) {
    data object Login : Screen("login")

    data object SignUp1 : Screen("signup1")

    data object SignUp2 : Screen("signup2")

    data object SignUp3 : Screen("signup3")

    data object SignUp4 : Screen("signup4")

    data object MyPage1 : Screen("myPage1")

    data object MyPage2 : Screen("myPage2")

    data object MyPage3 : Screen("myPage3")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeveryTime_Android2Theme {
                Navigation(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    @Composable
    fun Navigation(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        //이게 어디 어디에 네비바 넣을지 설정하는 코드
        val showBottomBar =
            currentRoute == Screen.MyPage1.route ||
                currentRoute == Screen.MyPage2.route

        Scaffold(
            modifier = modifier,
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                launchSingleTop = true
                            }
                        },
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.MyPage2.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = Screen.Login.route) { LoginScreen(navController) }
                composable(route = Screen.SignUp1.route) { SignUpScreen(navController) }
                composable(route = Screen.SignUp2.route) { SignUp2Screen(navController) }
                composable(route = Screen.SignUp3.route) { SignUp3Screen(navController) }
                composable(route = Screen.SignUp4.route) { SignUp4Screen(navController) }
                composable(route = Screen.MyPage1.route) { myPage1Screen(navController) }
                composable(route = Screen.MyPage2.route) { myPage2Screen(navController) }
                composable(route = Screen.MyPage3.route) { myPage3Screen(navController) }
            }
        }
    }

    @Composable
    private fun BottomNavigationBar(
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
                    //selected는 임시 코드 홈으로 바꿔야함
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
}
