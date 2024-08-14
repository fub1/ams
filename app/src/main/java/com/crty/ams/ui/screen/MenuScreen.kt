package com.crty.ams.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LeaveBagsAtHome
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.ui.viewmodel.LoginViewModel

import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.crty.ams.ui.theme.onPrimaryContainerLight
import com.crty.ams.ui.theme.primaryLight
import com.crty.ams.ui.viewmodel.MenuViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavHostController, viewModel: MenuViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.screen_menuScreen_topBar)


    val navController = rememberNavController()
//    var selectedTab by mutableStateOf("")

//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text(topBar) },
//                actions = {
//                    IconButton(onClick = { navController.navigate("loginSettings") }) {
//                        Icon(
//                            imageVector = Icons.Filled.ExitToApp,
//                            contentDescription = "Settings"
//                        )
//                    }
//                }
//            )
//        }
//    ) {
//        //布局内容
//
//    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(topBar) },
                actions = {
                    IconButton(onClick = { navController.navigate("loginSettings") }) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },

        bottomBar = {
            BottomNavigation (
//                modifier = Modifier.padding(bottom = 50.dp),
//                backgroundColor = primaryLight // 设置底部导航栏背景颜色
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Storage, contentDescription = "Asset Management") },
                    label = { Text("资产管理") },
                    selected = viewModel.selectedTab == "assets",
                    onClick = {
                        viewModel.selectedTab = "assets"
                        navController.navigate("assets")
                    },
                    selectedContentColor = Color.Yellow, // 设置选中项的颜色
                    unselectedContentColor = Color.Gray // 设置未选中项的颜色
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.LocalShipping, contentDescription = "Cargo Management") },
                    label = { Text("货物管理") },
                    selected = viewModel.selectedTab == "cargo",
                    onClick = {
                        viewModel.selectedTab = "cargo"
                        navController.navigate("cargo")
                    },
                    selectedContentColor = Color.Yellow, // 设置选中项的颜色
                    unselectedContentColor = Color.Gray // 设置未选中项的颜色
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Build, contentDescription = "Equipment Management") },
                    label = { Text("器具管理") },
                    selected = viewModel.selectedTab == "equipment",
                    onClick = {
                        viewModel.selectedTab = "equipment"
                        navController.navigate("equipment")
                    },
                    selectedContentColor = Color.Yellow, // 设置选中项的颜色
                    unselectedContentColor = Color.Gray // 设置未选中项的颜色
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "System Settings") },
                    label = { Text("系统设置") },
                    selected = viewModel.selectedTab == "settings",
                    onClick = {
                        viewModel.selectedTab = "settings"
                        navController.navigate("settings")
                    },
                    selectedContentColor = Color.Yellow, // 设置选中项的颜色
                    unselectedContentColor = Color.Gray // 设置未选中项的颜色
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "assets",
            Modifier.padding(innerPadding)
        ) {
            composable("assets") { AssetManagementScreen() }
            composable("cargo") { CargoManagementScreen() }
            composable("equipment") { EquipmentManagementScreen() }
            composable("settings") { SystemSettingsScreen() }
        }
    }
}

@Composable
fun AssetManagementScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Text(text = "资产管理", fontSize = 24.sp, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun CargoManagementScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Text(text = "货物管理", fontSize = 24.sp, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun EquipmentManagementScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Text(text = "器具管理", fontSize = 24.sp, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun SystemSettingsScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Text(text = "系统设置", fontSize = 24.sp, modifier = Modifier.fillMaxWidth())
    }
}

