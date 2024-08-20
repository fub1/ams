package com.crty.ams.core.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crty.ams.core.ui.component.LoadingContainer
import com.crty.ams.core.ui.component.EditableExposedDropdownMenu
import com.crty.ams.core.ui.viewmodel.AttributeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttributeBottomSheet(
    attributeName: String,
    onDismiss: () -> Unit
) {
    val viewModel: AttributeViewModel = hiltViewModel() // Use hiltViewModel() directly
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle events
    LaunchedEffect(uiState.message) {
        uiState.message.getValueOnce()?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Add icon (If permission granted)
            if (uiState.grandAddOrgPermission) {
                Button(onClick = {
                    // TODO: Implement add attribute logic
                    viewModel.addAttribute()
                }) {
                    Text("Add $attributeName")
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }

            // Title
            Text(text = attributeName, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.padding(8.dp))

            // 1st level dropdown
            EditableExposedDropdownMenu(
                menuTip = "Select 1st level $attributeName",
                options = uiState.firstLevelOptions.map { it.name },
                selectedOption = uiState.firstLevelString,
                onOptionSelected = { index ->
                    viewModel.onFirstLevelSelected(uiState.firstLevelOptions[index.toInt()])
                }
            )
            Spacer(modifier = Modifier.padding(8.dp))

            // 2nd level dropdown (Only show if there are options)
            if (uiState.secondLevelOptions.isNotEmpty()) {
                EditableExposedDropdownMenu(
                    menuTip = "Select 2nd level $attributeName",
                    options = uiState.secondLevelOptions.map { it.name },
                    selectedOption = uiState.secondLevelString,
                    onOptionSelected = { index ->
                        viewModel.onSecondLevelSelected(uiState.secondLevelOptions[index.toInt()])
                    }
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }

            // 3rd level dropdown (Only show if there are options)
            if (uiState.thirdLevelOptions.isNotEmpty()) {
                EditableExposedDropdownMenu(
                    menuTip = "Select 3rd level $attributeName",
                    options = uiState.thirdLevelOptions.map { it.name },
                    selectedOption = uiState.thirdLevelString,
                    onOptionSelected = { index ->
                        viewModel.onThirdLevelSelected(uiState.thirdLevelOptions[index.toInt()])
                    }
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }

            // Submit Button
            Button(onClick = {
                // TODO: Implement submit logic
                viewModel.submitAttribute()
                coroutineScope.launch {
                    // Show a success message or handle submission result
                    snackbarHostState.showSnackbar("Attribute submitted")
                }
            }) {
                Text("Submit")
            }

            // Snackbar
            SnackbarHost(hostState = snackbarHostState)

            // Loading indicator
            LoadingContainer(show = uiState.showLoading)
        }
    }
}

