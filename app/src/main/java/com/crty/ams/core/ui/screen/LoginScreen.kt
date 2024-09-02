// LoginScreen.kt
package com.crty.ams.core.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.core.ui.viewmodel.LoginViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crty.ams.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.screen_loginScreen_topBar)
    val usernameLabel = stringResource(R.string.screen_loginScreen_username_label)
    val passwordLabel = stringResource(R.string.screen_loginScreen_password_label)
    val loginButton = stringResource(R.string.screen_loginScreen_login_button)

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

//    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = uiState.value.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                label = { Text(usernameLabel) },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.value.usernameError != null
            )
            if (uiState.value.usernameError != null) {
                Text(
                    text = uiState.value.usernameError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.value.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text(passwordLabel) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.value.passwordError != null
            )
            if (uiState.value.passwordError != null) {
                Text(
                    text = uiState.value.passwordError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onLoginClick() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.value.isLoading
            ) {
                Text(loginButton)
            }

            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.value.isLoading
            ) {
                Text("to Home Screen")
            }

            // Observe the login state and navigate to the menu if logged in
//            println(uiState.)
//            LaunchedEffect(isLoggedIn) {
//                if (isLoggedIn) {
//                    navController.navigate("home") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                }
//            }
            // Show loading indicator if necessary
            if (uiState.value.isLoading) {
                CircularProgressIndicator()
            }
            // 测试
            Button(
                onClick = {
                    navController.navigate("attributeScreen")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Attribute Screen")
            }

            // 测试用
            Row {
                Button(
                    onClick = {
                        viewModel.fetchlocations()
                    },
                    enabled = true,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("取地点")
                }

                Button(
                    onClick = {
                        viewModel.fetchAssetCategory()
                    },
                    // enabled = false,
                    modifier = Modifier
                        .weight(1f)

                ) {
                    Text("取资产类")
                }

                Button(
                    onClick = {
                        viewModel.fetchdepartment()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("取部门")
                }

            }

            Row {
                Button(
                    onClick = {
                        viewModel.onLoginClick()
                    },
                    enabled = false,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("建地点")
                }

                Button(
                    onClick = {
                        viewModel.submitAsset()
                    },
                    // enabled = false,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("建资产类")
                }

                Button(
                    enabled = false,
                    onClick = {
                        viewModel.fetchdepartment()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("建部门")
                }

            }

            Row {
                Button(
                    onClick = {
                        viewModel.submitAssetRegister()
                    },
                    //enabled = false,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("资产REG")
                }

                Button(
                    onClick = {
                        viewModel.submitAsset()
                    },
                    enabled = false,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("资产查询")
                }

                Button(
                    enabled = false,
                    onClick = {
                        viewModel.fetchdepartment()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("资产绑定")
                }

            }

            Row {
                Button(
                    onClick = {
                        navController.navigate("rfid_demo")
                    },
                    // enabled = false,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("RFID")
                }

                Button(
                    onClick = {
                        viewModel.submitAsset()
                    },
                    enabled = false,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("K-Event")
                }

                Button(
                    enabled = false,
                    onClick = {
                        viewModel.fetchdepartment()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Bar-S")
                }

            }



        }
    }
}