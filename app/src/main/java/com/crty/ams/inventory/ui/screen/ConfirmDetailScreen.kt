package com.crty.ams.inventory.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.inventory.data.model.Asset
import com.crty.ams.inventory.ui.viewmodel.ConfirmDetailViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDetailScreen(navController: NavHostController, viewModel: ConfirmDetailViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_confirmDetailScreen_topBar)

    val assets by viewModel.assets.collectAsState()

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
    ) {innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
            .padding(innerPadding), // Apply padding to prevent overlap with topBar
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start ) {

            // 页面功能介绍文字
//            Text(
//                text = "本页面显示所有资产信息。点击右侧的删除按钮可以移除对应的资产。",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )

            // 资产信息列表
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(assets) { asset ->
                    AssetRow(asset = asset, onDeleteClick = { viewModel.deleteAsset(asset.id) })
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // 使按钮均匀分布
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp) // 添加间距，防止按钮紧挨着
                ) {
                    Text(text = "添加明细")
                }

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp) // 添加间距，防止按钮紧挨着
                ) {
                    Text(text = "确认创建")
                }
            }
        }

    }
}


@Composable
fun AssetRow(asset: Asset, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.LightGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "资产名称: ${asset.name}", fontWeight = FontWeight.Bold)
            Text(text = "资产编号: ${asset.code}")
            Text(text = "所属部门: ${asset.department}")
        }
        Button(onClick = onDeleteClick) {
            Text(text = "删除")
        }
    }
}