package com.example.deverytime_android2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deverytime_android2.ui.theme.DeveryTime_Android2Theme

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp1 : Screen("signup1")
    data object SignUp2 : Screen("signup2")
    data object SignUp3 : Screen("signup3")
    data object SignUp4 : Screen("signup4")
    data object OnBoard1 : Screen(route = "OnBoard1")
    data object OnBoard2 : Screen(route = "OnBoard2")
    data object OnBoard3 : Screen(route = "OnBoard3")
    data object OnBoard4 : Screen(route = "OnBoard4")
    data object OnBoard5 : Screen(route = "OnBoard5")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeveryTime_Android2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Navigation(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

    @Composable
    fun Navigation(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen.OnBoard1.route,
            modifier = modifier
        ) {
            composable(route = Screen.Login.route) { LoginScreen(navController) }
            composable(route = Screen.OnBoard1.route) { OnBoard1Screen(navController) }
            composable(route = Screen.OnBoard2.route) { OnBoard2Screen(navController) }
            composable(route = Screen.OnBoard3.route) { OnBoard3Screen(navController) }
            composable(route = Screen.OnBoard4.route) { OnBoard4Screen(navController) }
            composable(route = Screen.OnBoard5.route) { OnBoard5Screen(navController) }
            composable(route = Screen.SignUp1.route) { SignUpScreen(navController) }
            composable(route = Screen.SignUp2.route) { SignUp2Screen(navController) }
            composable(route = Screen.SignUp3.route) { SignUp3Screen(navController) }
            composable(route = Screen.SignUp4.route) { SignUp4Screen(navController) }
        }
    }
}
