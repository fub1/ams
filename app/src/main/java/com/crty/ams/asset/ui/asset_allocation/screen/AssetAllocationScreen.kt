package com.crty.ams.asset.ui.asset_allocation.screen

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.asset.ui.asset_allocation.viewmodel.AssetAllocationViewModel
import com.crty.ams.core.ui.component.compose.picker.AttributePage
import com.crty.ams.core.ui.component.compose.single_field_roller.SingleFieldRollerScreen
import com.crty.ams.core.ui.component.compose.user_single_roller.UserSingleRollerScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AssetAllocationScreen(
    navController: NavHostController,
    ids: List<Int>,
    viewModel: AssetAllocationViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_assetAllocationScreen_topBar)

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

    fun submit(){
        locationError = false
        locationLabel = "* 请选择使用位置"
        departmentError = false
        departmentLabel = "* 请选择使用部门"
        var pass = true
        if (locationId == 0){
            locationError = true
            locationLabel = "使用位置不能为空"
            pass = false
        }
        if (departmentId == 0){
            departmentError = true
            departmentLabel = "使用部门不能为空"
            pass = false
        }

        if (pass){
            viewModel.submit()
        }
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
            innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(innerPadding), // Apply padding to prevent overlap with topBar
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextFieldWithLabel(
                text = "使用位置",
                label = locationLabel,
                value = location,
                onValueChange = {
                    locationError = false
                    locationLabel = "* 请选择使用位置"
                },
                onClick = {
                    // Handle click event here
                    selectedAttributeType.value = "使用位置"
                    showSheet.value = true

                },
                showError = locationError,
            )
            TextFieldWithLabel(
                text = "使用部门",
                label = departmentLabel,
                value = department,
                onValueChange = {
                    departmentError = false
                    departmentLabel = "* 请选择使用部门"
                },
                onClick = {
                    // Handle click event here
                    selectedAttributeType.value = "使用部门"
                    showSheet.value = true

                },
                showError = departmentError,
            )
            TextFieldWithLabel(
                text = "使用人",
                label = userLabel,
                value = user,
                onValueChange = {  },
                onClick = {
                    // Handle click event here
//                    selectedAttributeType.value = "使用人"
//                    showSheet.value = true
                    showUserSheet.value = true
                },
                showError = userError,
            )
            Spacer(modifier = Modifier.height(16.dp)) // 间距

            Button(
                onClick = {
                    /* 跳转到批量修改页面 */
//                    viewModel.submit()
                    submit()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("确认调拨")
            }


            // Display the ModalBottomSheet
            if (showSheet.value) {
                AttributePage(
                    attributeType = selectedAttributeType.value,
                    showSheet = showSheet,
                    navController,
                    onDismiss = { data, number ->
                        // 回传数据给 AssetRegisterScreen 的 ViewModel
                        when(selectedAttributeType.value){
                            "使用位置" -> {
                                viewModel.updateLocationValueId(data, number)
                                showSheet.value = false // 关闭 ModalBottomSheet
                            }
                            "使用部门" -> {
                                viewModel.updateDepartmentValueId(data, number)
                                showSheet.value = false // 关闭 ModalBottomSheet
                            }
//                            "使用人" -> {
//                                viewModel.updateUserValueId(data, number)
//                                showSheet.value = false // 关闭 ModalBottomSheet
//                            }
                        }
                    }
                )
            }
            if (showUserSheet.value){
                UserSingleRollerScreen(
                    departmentId,
                    showUserSheet,
                    onConfirm = {

                    },
                    onDismiss = { id, name, dId, dName ->
                        // 回传数据给 AssetRegisterScreen 的 ViewModel
                        viewModel.updateUserValueId(id, name, dId, dName)
                        showUserSheet.value = false // 关闭 ModalBottomSheet
                    }
                )
            }
        }
// 加载动画
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()  // 显示加载动画
            }
        }
        if (isTimeout) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissTimeoutDialog() },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissTimeoutDialog() }) {
                        Text("确定")
                    }
                },
                title = { Text("请求超时") },
                text = { Text("请求登记接口超时，请重试。") }
            )
        }
        if (isFailed) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissFieldDialog() },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissFieldDialog() }) {
                        Text("确定")
                    }
                },
                title = { Text("登记失败") },
                text = { Text(failedMessage) }
            )
        }
        if (isError) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissErrorDialog() },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissErrorDialog() }) {
                        Text("确定")
                    }
                },
                title = { Text("操作异常") },
                text = { Text("登记过程中出现异常。") }
            )
        }


    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldWithLabel(
    text: String,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    showError: Boolean, // 用于控制是否显示错误状态
) {
    Column {
        Text(text = text, style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInteropFilter {
                    if (it.action == MotionEvent.ACTION_DOWN) {
                        onClick()
                    }
                    false
                },
            label = {
                Text(
                    label
                )
            },
            singleLine = true,
            isError = showError
        )
    }
}