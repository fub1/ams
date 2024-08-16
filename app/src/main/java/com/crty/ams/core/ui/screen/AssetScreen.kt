package com.crty.ams.core.ui.screen

import android.annotation.SuppressLint
import android.content.res.Resources
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.core.ui.viewmodel.AssetViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            navController.navigate("compose"){
                popUpTo("home") { inclusive = true }
            }
        }
        2 -> println("Favorites button clicked")
        3 -> println("Settings button clicked")
        4 -> println("Profile button clicked")
        5 -> println("Messages button clicked")
        6 -> println("Logout button clicked")
        7 -> println("Help button clicked")
        8 -> println("Feedback button clicked")
        9 -> println("About button clicked")
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