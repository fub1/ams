package com.crty.ams.core.ui.compose.multilevel_list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.crty.ams.core.data.model.User
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crty.ams.inventory.data.model.Asset

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultilevelListScreen(
    viewModel: MultilevelListViewModel = hiltViewModel()
) {
    val assets by viewModel.assets.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(assets) { asset ->
            AssetItem(asset, viewModel)
        }
    }
}

@Composable
fun AssetItem(asset: Asset, viewModel: MultilevelListViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
//                .padding(8.dp)
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

            // 移除按钮
            IconButton(onClick = { viewModel.removeAsset(asset.id) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "移除")
            }
        }

        // 展开子资产
        if (expanded && asset.subAssets != null) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                asset.subAssets.forEach { subAsset ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Spacer(modifier = Modifier.size(24.dp)) // 占位符，保持与上级资产信息对齐
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "名称: ${subAsset.name}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "编号: ${subAsset.code}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "部门: ${subAsset.department}", style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.removeAsset(subAsset.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "移除")
                        }
                    }
                }
            }
        }
    }
}