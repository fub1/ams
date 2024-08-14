package com.crty.ams.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.crty.ams.ui.screen.LoginScreen
import com.crty.ams.ui.screen.LoginSettingsScreen
import com.crty.ams.ui.screen.MenuScreen
import com.crty.ams.ui.viewmodel.LoginViewModel

@Composable
fun AppNavigation(start: RouteList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = start.description) {
        composable(route = RouteList.Login.description) {
            LoginScreen(navController)
        }
        composable(route = RouteList.Settings.description) {
            LoginSettingsScreen(navController)
        }
        composable(route = RouteList.Menu.description) {
            MenuScreen(navController)
        }
    }
}

// 路由列表
// 可以用来关联一个导航标题名称
enum class RouteList(val description: String) {
    Login("login"),
    Settings("settings"),
    Menu("menu")
}