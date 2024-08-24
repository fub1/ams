package com.crty.ams.inventory.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.inventory.ui.viewmodel.CreateInventoryViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInventoryScreen(navController: NavHostController, viewModel: CreateInventoryViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_createInventoryScreen_topBar)

    // 定义三个状态变量来控制每个文本框是否启用
    val isTextField1Enabled by viewModel.isTextField1Enabled.collectAsState()
    val isTextField2Enabled by viewModel.isTextField2Enabled.collectAsState()
    val isTextField3Enabled by viewModel.isTextField3Enabled.collectAsState()

    val textField1Value by viewModel.textField1Value.collectAsState()
    val textField2Value by viewModel.textField2Value.collectAsState()
    val textField3Value by viewModel.textField3Value.collectAsState()

    val isToggleEnabled by viewModel.isToggleEnabled.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()


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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(innerPadding), // Apply padding to prevent overlap with topBar
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "请选择创建盘点单规则。",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "请选择相应的单选框以启用下方的输入框",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 第一组单选框和文本输入框
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = isTextField1Enabled,
                    onClick = { viewModel.updateTextField1Enabled(!isTextField1Enabled) },
                    modifier = Modifier.size(16.dp)
                )
                OutlinedTextField(
                    value = textField1Value,
                    onValueChange = { viewModel.updateTextField1Value(it) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isTextField1Enabled,
                    label = { Text("按照部门筛选") }
                )
            }

            // 第二组单选框和文本输入框
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = isTextField2Enabled,
                    onClick = { viewModel.updateTextField2Enabled(!isTextField2Enabled) },
                    modifier = Modifier.size(16.dp)
                )
                OutlinedTextField(
                    value = textField2Value,
                    onValueChange = { viewModel.updateTextField2Value(it) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isTextField2Enabled,
                    label = { Text("按照使用位置筛选") }
                )
            }

            // 第三组单选框和文本输入框
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = isTextField3Enabled,
                    onClick = { viewModel.updateTextField3Enabled(!isTextField3Enabled) },
                    modifier = Modifier.size(16.dp)
                )
                OutlinedTextField(
                    value = textField3Value,
                    onValueChange = { viewModel.updateTextField3Value(it) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isTextField3Enabled,
                    label = { Text("按照使用人筛选") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 控制是否启用左右选择组件的单选框
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = isToggleEnabled,
                    onClick = { viewModel.updateToggleEnabled(!isToggleEnabled) },
                    modifier = Modifier.size(16.dp)
                )
                Text(text = "按照价值筛选")
            }

            // 左右选择组件
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "最低价值", modifier = Modifier.padding(end = 8.dp))
                RadioButton(
                    selected = selectedOption == "最低价值",
                    onClick = { viewModel.updateSelectedOption("最低价值") },
                    enabled = isToggleEnabled,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = selectedOption == "最高价值",
                    onClick = { viewModel.updateSelectedOption("最高价值") },
                    enabled = isToggleEnabled,
                    modifier = Modifier.size(16.dp)
                )
                Text(text = "最高价值", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("confirmDetail")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("下一步")
            }
        }
    }
}