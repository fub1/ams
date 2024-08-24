package com.crty.ams.asset.ui.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.R
import com.crty.ams.asset.ui.viewmodel.AssetCheckViewModel
import com.crty.ams.asset.ui.viewmodel.AssetRegisterViewModel
import com.crty.ams.core.ui.compose.picker.AttributePage
import com.crty.ams.core.ui.compose.picker.AttributeViewModel
import com.crty.ams.core.ui.theme.AmsTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetCheckViewModel(navController: NavHostController, viewModel: AssetCheckViewModel = hiltViewModel()) {
    val topBar = stringResource(R.string.asset_screen_assetCheckScreen_topBar)

    val asset by viewModel.asset.collectAsState()

    // Get the context here
    val context = LocalContext.current


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
            viewModel.setScreenValue()

            TextFieldWithLabel(
                text = "资产代码",
                label = "请输入资产代码",
                value = asset.asset_code
            )
            TextFieldWithLabel(
                text = "资产名称",
                label = "请输入资产名称",
                value = asset.asset_name
            )
            // Add other TextFieldWithLabel here for input3 to input9...
            TextFieldWithLabel(
                text = "资产分类",
                label = "请选择资产分类",
                value = asset.asset_category
            )
            TextFieldWithLabel(
                text = "品牌",
                label = "请输入品牌",
                value = asset.brand
            )
            TextFieldWithLabel(
                text = "型号",
                label = "请输入型号",
                value = asset.model
            )
            TextFieldWithLabel(
                text = "序列号",
                label = "请输入序列号",
                value = asset.sn
            )
            TextFieldWithLabel(
                text = "供应商",
                label = "请输入供应商",
                value = asset.supplier
            )
            TextFieldWithLabel(
                text = "采购日期",
                label = "请选择采购日期",
                value = asset.purchase_date
            )
            TextFieldWithLabel(
                text = "价格",
                label = "请输入价格",
                value = asset.price
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldWithLabel(
    text: String,
    label: String,
    value: String
) {
    Column {
        Text(text = text, style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    text = label
                )
            },
            singleLine = true,
            enabled = false, // 设置为禁用状态
        )
    }
}