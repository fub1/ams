package com.crty.ams.inventory.ui.screen

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.core.ui.theme.AmsTheme
import com.crty.ams.core.ui.viewmodel.LoginViewModel
import com.crty.ams.inventory.ui.viewmodel.InventoryListViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryListScreen(navController: NavHostController, viewModel: InventoryListViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_inventoryListScreen_topBar)


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(topBar) },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AmsTheme{
                LargeFloatingActionButton(onClick = {
                    // Handle FAB click action here
                    navController.navigate("createInventory")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End // Position FAB to the end (bottom-right) by default
    ) {

    }
}