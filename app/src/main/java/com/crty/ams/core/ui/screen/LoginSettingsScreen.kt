package com.crty.ams.core.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.core.ui.viewmodel.LoginSettingsViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSettingsScreen(navController: NavHostController, viewModel: LoginSettingsViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showApiStatus by remember { mutableStateOf(false) }
    var apiStatusMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                CenterAlignedTopAppBar(
                    title = { Text("Login Settings") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.value.serverAddress,
                    onValueChange = { viewModel.onServerAddressChanged(it) },
                    label = { Text("Server Address (e.g., http://example.com)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.value.serverPort.toString(),
                    onValueChange = { newValue ->
                        viewModel.onServerPortChanged(newValue)
                    },
                    label = { Text("Server Port") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.onSaveSettingsClick()
                                snackbarHostState.showSnackbar(
                                    message = "Settings saved successfully!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Settings")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val result = viewModel.checkApiStatus()
                                apiStatusMessage = if (result.isSuccess) {
                                    "API Status: OK (Version: ${result.getOrNull()?.version}, Timestamp: ${result.getOrNull()?.timestamp})"
                                } else {
                                    "API Status: Error (${result.exceptionOrNull()?.message})"
                                }
                                showApiStatus = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Check")
                    }
                }

                if (showApiStatus) {
                    Text(apiStatusMessage)
                }
            }
        }
    }
}