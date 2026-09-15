package com.example.deverytime_android2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.BottomNavigationBar
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme

sealed class Screen(
    val route: String,
) {
    data object Login : Screen("login")

    data object SignUp1 : Screen("signUp1")

    data object SignUp2 : Screen("signUp2")

    data object SignUp3 : Screen("signUp3")

    data object SignUp4 : Screen("signUp4")

    data object MyPage1 : Screen("myPage1")

    data object MyPage2 : Screen("myPage2")

    data object MyPage3 : Screen("myPage3")

    data object OnBoard1 : Screen(route = "onBoard1")

    data object OnBoard2 : Screen(route = "onBoard2")

    data object OnBoard3 : Screen(route = "onBoard3")

    data object OnBoard4 : Screen(route = "onBoard4")

    data object OnBoard5 : Screen(route = "onBoard5")

    data object PostView : Screen(route = "postView")

    data object Posting : Screen(route = "posting"
    
    data object  Main1 : Screen(route = "Main1")
    
    data object  Main2 : Screen(route = "Main2")
    
    data object  Main3 : Screen(route = "Main3")
    
    data object  Main4 : Screen(route = "Main4")
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

        // 이게 어디 어디에 네비바 넣을지 설정하는 코드
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
                                popUpTo(Screen.MyPage1.route)
                                launchSingleTop = true
                            }
                        },
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.OnBoard1.route,
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding),
            ) {
                composable(route = Screen.Login.route) { LoginScreen(navController) }
                composable(route = Screen.SignUp1.route) { SignUpScreen(navController) }
                composable(route = Screen.SignUp2.route) { SignUp2Screen(navController) }
                composable(route = Screen.SignUp3.route) { SignUp3Screen(navController) }
                composable(route = Screen.SignUp4.route) { SignUp4Screen(navController) }
                composable(route = Screen.MyPage1.route) { myPage1Screen(navController) }
                composable(route = Screen.MyPage2.route) { myPage2Screen(navController) }
                composable(route = Screen.MyPage3.route) { myPage3Screen(navController) }
                composable(route = Screen.OnBoard1.route) { OnBoard1Screen(navController) }
                composable(route = Screen.OnBoard2.route) { OnBoard2Screen(navController) }
                composable(route = Screen.OnBoard3.route) { OnBoard3Screen(navController) }
                composable(route = Screen.OnBoard4.route) { OnBoard4Screen(navController) }
                composable(route = Screen.OnBoard5.route) { OnBoard5Screen(navController) }
                composable(route = Screen.Posting.route) { PostingScreen(navController) }
                composable(route = Screen.PostView.route) { PostViewScreen(navController) }
                composable(route = Screen.Main1.route) { Main1Screen(navController) }
                composable(route = Screen.Main2.route) { Main2Screen(navController) }
                composable(route = Screen.Main3.route) { Main3Screen(navController) }
                composable(route = Screen.Main4.route) { Main4Screen(navController) }
            }
        }
    }
}
