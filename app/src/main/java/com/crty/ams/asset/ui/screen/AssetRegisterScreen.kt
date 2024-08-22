package com.crty.ams.asset.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.asset.ui.viewmodel.AssetRegisterViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetRegisterScreen(navController: NavHostController, viewModel: AssetRegisterViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_assetRegisterScreen_topBar)

    val input1 by viewModel.input1.collectAsState()
    val input2 by viewModel.input2.collectAsState()
    val input3 by viewModel.input3.collectAsState()
    val input4 by viewModel.input4.collectAsState()
    val input5 by viewModel.input5.collectAsState()
    val input6 by viewModel.input6.collectAsState()
    val input7 by viewModel.input7.collectAsState()
    val input8 by viewModel.input8.collectAsState()
    val input9 by viewModel.input9.collectAsState()
    // Collect other inputs similarly...

    // Get the context here
    val context = LocalContext.current


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(topBar) },
                actions = {
                    IconButton(onClick = { navController.navigate("loginSettings") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        contentPadding = PaddingValues(top = 56.dp) // Adjust padding based on your top bar height
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextFieldWithLabel(
                label = "Input 1",
                value = input1,
                onValueChange = { viewModel.onInput1Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 1 clicked", Toast.LENGTH_SHORT).show()
                }
            )
            TextFieldWithLabel(
                label = "Input 2",
                value = input2,
                onValueChange = { viewModel.onInput2Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 2 clicked", Toast.LENGTH_SHORT).show()
                }
            )
            // Add other TextFieldWithLabel here for input3 to input9...
            TextFieldWithLabel(
                label = "Input 3",
                value = input3,
                onValueChange = { viewModel.onInput3Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 3 clicked", Toast.LENGTH_SHORT).show()
                }
            )
            TextFieldWithLabel(
                label = "Input 4",
                value = input4,
                onValueChange = { viewModel.onInput4Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 4 clicked", Toast.LENGTH_SHORT).show()
                }
            )
            TextFieldWithLabel(
                label = "Input 5",
                value = input5,
                onValueChange = { viewModel.onInput5Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 5 clicked", Toast.LENGTH_SHORT).show()
                }
            )
            TextFieldWithLabel(
                label = "Input 6",
                value = input6,
                onValueChange = { viewModel.onInput6Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 6 clicked", Toast.LENGTH_SHORT).show()
                }
            )
            TextFieldWithLabel(
                label = "Input 7",
                value = input7,
                onValueChange = { viewModel.onInput7Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 7 clicked", Toast.LENGTH_SHORT).show()
                }
            )
            TextFieldWithLabel(
                label = "Input 8",
                value = input8,
                onValueChange = { viewModel.onInput8Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 8 clicked", Toast.LENGTH_SHORT).show()
                }
            )
            TextFieldWithLabel(
                label = "Input 9",
                value = input9,
                onValueChange = { viewModel.onInput9Changed(it) },
                onClick = {
                    // Handle click event here
                    Toast.makeText(context, "Input 9 clicked", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }



}

@Composable
fun TextFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }, // Clickable modifier
            label = { Text(text = label) },
            singleLine = true
        )
    }
}