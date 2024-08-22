package com.crty.ams.core.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.crty.ams.asset.ui.screen.AssetScreen
import com.crty.ams.cargo.ui.screen.CargoScreen
import com.crty.ams.center.ui.screen.SystemSettingsScreen
import com.crty.ams.container.ui.screen.ContainerScreen
import com.crty.ams.core.ui.viewmodel.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.screen_menuScreen_topBar)

    val topBarTitle by viewModel.topBarTitle.collectAsState()


    val homeNavController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(topBarTitle) },
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
            var selectedItem by remember { mutableIntStateOf(0) }
            val items = listOf("资产管理", "货物管理", "器具管理", "系统设置")
            val routeItems = listOf("home/asset", "home/cargo", "home/container", "home/systemSettings")
            val iconItems = listOf(Icons.Filled.Storage, Icons.Filled.LocalShipping, Icons.Filled.Build, Icons.Filled.Settings)
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(iconItems[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            homeNavController.navigate(routeItems[index])
                            viewModel.screenSwitch(items[index])
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = homeNavController,
            startDestination = "home/asset",
            Modifier.padding(innerPadding)
        ) {
            composable("home/asset") { AssetScreen(navController) }
            composable("home/cargo") { CargoScreen(navController) }
            composable("home/container") { ContainerScreen(navController) }
            composable("home/systemSettings") { SystemSettingsScreen(navController) }
        }
    }
}

