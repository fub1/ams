package com.crty.ams.core.ui.compose.picker

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crty.ams.core.ui.component.compose.picker.AttributeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttributeScreen(viewModel: AttributeViewModel = hiltViewModel()) {
    val showSheet = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attribute Page") }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { showSheet.value = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Show Attribute Options")
            }

            // Optionally, add more content here
        }

        // Show the bottom sheet when showSheet.value is true
//            if (showSheet.value) {
//                AttributePage(
//                    attributeType = "asset",
//                    showSheet = showSheet,
//                    viewModel = viewModel,
//                    navController =
//                )
//            }
    }
}

