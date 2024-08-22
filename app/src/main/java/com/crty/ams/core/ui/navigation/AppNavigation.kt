package com.crty.ams.core.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crty.ams.core.ui.screen.AttributeScreen
import com.crty.ams.core.ui.screen.LoginScreen
import com.crty.ams.core.ui.screen.LoginSettingsScreen

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

        composable("attributeScreen") {
            val showSheet = remember { mutableStateOf(false) }
            AttributeScreen(
                attributeType = "asset",
                showSheet = showSheet,
                //onConfirm = {},
                //onCancel = {},
                // onBack = { navController.popBackStack() }
            )
        }
    }
}