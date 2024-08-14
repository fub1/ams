package com.crty.ams.ui.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crty.ams.ui.screen.LoginScreen
import com.crty.ams.ui.screen.LoginSettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("loginSettings") {
            LoginSettingsScreen(navController)
        }
    }
}