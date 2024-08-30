package com.crty.ams.asset.ui.asset_menu.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.asset.ui.asset_menu.viewmodel.AssetViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import com.crty.ams.core.ui.theme.primaryLight
import androidx.compose.material3.ButtonDefaults

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetScreen(navController: NavHostController, viewModel: AssetViewModel = hiltViewModel()) {
    // 直接访问 viewModel.menuItems
    val menuItems = remember { viewModel.menuItems }


    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(menuItems.size) { index ->
            val menuItem = menuItems[index]
            SquareButton(
                onClick = {
                    /* Handle button click */
                    handleButtonClick(menuItem.id, navController)
                },
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(menuItem.icon, contentDescription = menuItem.title)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = menuItem.title)
                }
            }
        }
    }
}

private fun handleButtonClick(id: Int, navController: NavHostController) {
    // Handle button clicks based on ID
    when (id) {
        1 -> {
            println("Home button clicked")
            navController.navigate("assetRegister/111222/qqwwqqww"){
                popUpTo("home") { inclusive = true }
            }
        }
        2 -> {
            println("Favorites button clicked")
            navController.navigate("camera"){
                popUpTo("home") { inclusive = true }
            }
        }
        3 -> {
            println("Settings button clicked")
            navController.navigate("picker"){
                popUpTo("home") { inclusive = true }
            }
        }
        4 -> {
            println("p button clicked")
            navController.navigate("assetCheck"){
                popUpTo("home") { inclusive = true }
            }
        }
        5 -> {
            println("m button clicked")
            navController.navigate("inventoryList"){
                popUpTo("home") { inclusive = true }
            }
        }
        6 -> {
            println("out button clicked")
            navController.navigate("multilevelListTest"){
                popUpTo("home") { inclusive = true }
            }
        }
        7 -> {
        println("help button clicked")
        navController.navigate("assetChangeSingle"){
            popUpTo("home") { inclusive = true }
        }
    }
        8 -> {
            println("batch button clicked")
            navController.navigate("assetChangeBatch") {
                popUpTo("home") { inclusive = true }
            }
        }
        9 -> {
            println("allocation button clicked")
            navController.navigate("assetAllocation") {
                popUpTo("home") { inclusive = true }
            }
        }
        10 -> println("Terms button clicked")
        else -> println("Unknown button clicked")
    }
}

// Define a custom button style for a square button
@Composable
fun SquareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp), // 设置圆角
        modifier = modifier
            .aspectRatio(1f) // Ensure the button is a square
            .background(primaryLight, shape = MaterialTheme.shapes.medium),
        colors = ButtonDefaults.buttonColors(primaryLight),
        contentPadding = PaddingValues(4.dp) // Remove default padding
    ) {
        content()
    }
}