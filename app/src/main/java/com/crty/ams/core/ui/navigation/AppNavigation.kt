package com.crty.ams.core.ui.navigation


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crty.ams.core.ui.compose.roll_list.ComposeScreen
import com.crty.ams.core.ui.screen.HomeScreen
import com.crty.ams.core.ui.screen.LoginScreen
import com.crty.ams.core.ui.screen.LoginSettingsScreen
import org.imaginativeworld.whynotcompose.ui.screens.tutorial.captureimageandcrop.CaptureImageAndCropScreen
import org.imaginativeworld.whynotcompose.ui.screens.tutorial.captureimageandcrop.CaptureImageAndCropViewModel

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
        composable(route = RouteList.Home.description) {
            HomeScreen(navController)
        }
        composable(route = RouteList.Test.description) {
            ComposeScreen(navController)
        }
        composable(route = RouteList.Picker.description) {
            val viewModel: CaptureImageAndCropViewModel = hiltViewModel()

            CaptureImageAndCropScreen(
                viewModel = viewModel,
                goBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}


// 路由列表
// 可以用来关联一个导航标题名称
enum class RouteList(val description: String) {
    Login("login"),
    Settings("settings"),
    Home("home"),
    Test("compose"),
    Picker("picker")
}