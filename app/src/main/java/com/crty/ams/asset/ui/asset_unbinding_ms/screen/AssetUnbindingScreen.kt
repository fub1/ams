package com.crty.ams.asset.ui.asset_unbinding_ms.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.crty.ams.R
import com.crty.ams.asset.data.network.model.AssetForGroup
import com.crty.ams.asset.data.network.model.AssetForList
import com.crty.ams.asset.ui.asset_register.viewmodel.AssetRegisterViewModel
import com.crty.ams.asset.ui.asset_unbinding_ms.viewmodel.AssetUnbindingViewModel
import com.crty.ams.core.ui.compose.multilevel_list.MultilevelListViewModel
import com.crty.ams.core.ui.theme.AmsTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetUnbindingScreen(navController: NavHostController,
                         asset: List<AssetForGroup>,
                         viewModel: AssetUnbindingViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_assetUnbindingScreen_topBar_MS)

// Get the context here
    val context = LocalContext.current

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val assets by viewModel.assets.collectAsState()

    val selectedAssets by viewModel.selectedSubAssets.collectAsState()
    // 监听 ViewModel 中的弹窗显示状态
    val showSuccessPopup by viewModel.showSuccessPopup

    val isLoading by viewModel.isLoading.collectAsState()
    val isTimeout by viewModel.isTimeout.collectAsState()
    val isFailed by viewModel.isFailed.collectAsState()
    val failedMessage by viewModel.failedMessage.collectAsState()
    val isError by viewModel.isError.collectAsState()

    //副作用获取要解绑的组资产信息
    LaunchedEffect(Unit) {
        viewModel.fetchAllAttributes(asset)
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
//                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(innerPadding), // Apply padding to prevent overlap with topBar
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "要解绑的资产组",
                style = MaterialTheme.typography.titleLarge // 标题样式
            )
            Spacer(modifier = Modifier.height(8.dp)) // 间距

            LazyColumn(
                modifier = Modifier
                    .height(screenHeight * 2 / 3)
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(assets) { asset ->
                    AssetItem(asset, viewModel, selectedAssets)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // 使按钮均匀分布
            ) {
                Button(
                    onClick = {
                    /*TODO*/
                        viewModel.unbindAll()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp) // 添加间距，防止按钮紧挨着
                ) {
                    Text(text = "全部解绑")
                }

                Button(
                    onClick = {
                    /*TODO*/
                        val newAttributesList = mutableListOf<Int>()
                        selectedAssets.forEach { asset ->
                            println("选中的资产: ${asset.id} ${asset.name} (${asset.code})")
                            newAttributesList.add(asset.id)
                        }
                        viewModel.unbindPart(newAttributesList)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp) // 添加间距，防止按钮紧挨着
                ) {
                    Text(text = "部分解绑")
                }

                // 显示弹窗
                if (showSuccessPopup) {
                    SuccessPopup()
                }
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

@Composable
fun AssetItem(asset: AssetForList, viewModel: AssetUnbindingViewModel, selectedAssets: Set<AssetForList>) {
    var expanded by remember { mutableStateOf(false) }
//    val selectedSubAssets = remember { mutableStateListOf<AssetForList>() }  // 用于存储选中的二级资产信息

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // 展开/折叠图标
            if (asset.hasSubAssets) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = "展开/折叠",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp)
                        .clickable { expanded = !expanded }
                )
            } else {
                Spacer(modifier = Modifier.size(40.dp))
            }

            // 资产信息
            Column(modifier = Modifier.weight(1f)) {
                val title = if (asset.hasSubAssets) "[资产组]" else "名称"
                Text(text = "$title: ${asset.name}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "编号: ${asset.code}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "部门: ${asset.department}", style = MaterialTheme.typography.bodySmall)
            }
        }

        // 展开子资产
        if (expanded) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                asset.subAssetsForCheck.forEach { subAsset ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        // 复选框，用于选择二级资产
                        Checkbox(
                            checked = selectedAssets.contains(subAsset),
                            onCheckedChange = { isChecked ->
                                viewModel.toggleSubAssetSelection(subAsset, isChecked)
                            }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "名称: ${subAsset.name}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "编号: ${subAsset.code}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "部门: ${subAsset.department}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessPopup() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false // 允许我们自定义 Dialog 的宽度
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x15000000)) // 设置 Dialog 的半透明背景
                .padding(32.dp), // 设置内容与边缘的距离
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Lottie 动画
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        iterations = 1
                    )
                    LottieAnimation(
                        composition = composition,
                        progress = progress,
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 显示 "注册成功" 的文本
                    Text(text = "解绑成功", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
