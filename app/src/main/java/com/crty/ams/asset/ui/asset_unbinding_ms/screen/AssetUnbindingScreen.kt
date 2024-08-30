package com.crty.ams.asset.ui.asset_unbinding_ms.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.asset.data.network.model.AssetForList
import com.crty.ams.asset.ui.asset_register.viewmodel.AssetRegisterViewModel
import com.crty.ams.asset.ui.asset_unbinding_ms.viewmodel.AssetUnbindingViewModel
import com.crty.ams.core.ui.compose.multilevel_list.MultilevelListViewModel
import com.crty.ams.core.ui.theme.AmsTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetUnbindingScreen(navController: NavHostController, viewModel: AssetUnbindingViewModel) {
    val topBar = stringResource(R.string.asset_screen_assetUnbindingScreen_topBar_MS)

// Get the context here
    val context = LocalContext.current

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val assets by viewModel.assets.collectAsState()

    val selectedAssets by viewModel.selectedSubAssets.collectAsState()

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
                        selectedAssets.forEach { asset ->
                            println("选中的资产: ${asset.id} ${asset.name} (${asset.code})")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp) // 添加间距，防止按钮紧挨着
                ) {
                    Text(text = "部分解绑")
                }
            }

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

    // 将选中的子资产传递给 ViewModel
//    LaunchedEffect(selectedSubAssets) {
//        viewModel.updateSelectedSubAssets(selectedSubAssets)
//        println("修改勾选框的值")
//    }
}
