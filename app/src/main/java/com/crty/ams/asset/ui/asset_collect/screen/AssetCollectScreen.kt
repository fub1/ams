package com.crty.ams.asset.ui.asset_collect.screen

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.asset.ui.asset_allocation.viewmodel.AssetAllocationViewModel
import com.crty.ams.asset.ui.asset_collect.viewmodel.AssetCollectViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AssetCollectScreen(
    navController: NavHostController,
    ids: List<Int>,
    viewModel: AssetCollectViewModel = hiltViewModel()
) {
    val topBar = stringResource(R.string.asset_screen_assetCollectScreen_topBar)

    val location by viewModel.location.collectAsState()
    val locationId by viewModel.locationId.collectAsState()
    val departmentId by viewModel.departmentId.collectAsState()
    val department by viewModel.department.collectAsState()
    val user by viewModel.user.collectAsState()

    // State to control the visibility of the ModalBottomSheet
    val showSheet = remember { mutableStateOf(false) }
    val showUserSheet = remember { mutableStateOf(false) }
    val selectedAttributeType = remember { mutableStateOf("") }

    var locationError by remember { mutableStateOf(false) }
    var locationLabel by remember { mutableStateOf("* 请选择使用位置") }
    var departmentError by remember { mutableStateOf(false) }
    var departmentLabel by remember { mutableStateOf("* 请选择使用部门") }
    var userError by remember { mutableStateOf(false) }
    var userLabel by remember { mutableStateOf("请选择使用人") }


    val isLoading by viewModel.isLoading.collectAsState()
    val isTimeout by viewModel.isTimeout.collectAsState()
    val isFailed by viewModel.isFailed.collectAsState()
    val failedMessage by viewModel.failedMessage.collectAsState()
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchData(ids)
    }


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
        }
    ) {

    }
}