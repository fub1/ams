package com.crty.ams.core.ui.compose.roll_list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.core.ui.viewmodel.AssetViewModel
import com.example.citypicker.SimonCityPicker

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeScreen(navController: NavHostController, viewModel: AssetViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var selectedCity by remember { mutableStateOf("请选择城市") }
    var isCity by remember { mutableStateOf(false) }
    Scaffold{
        listCompose()
        SimonCityPicker(
            context = context,
            isCity = isCity,
            onCitySelected = { city ->
                selectedCity=city
                isCity = false
            })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun listCompose(){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("组件测试页面") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Column {
                SimpleTable(menuItems = listOf(
                    MenuItem("资产名", 10),
                    MenuItem("部门", 20),
                    MenuItem("编号", 30),
                    MenuItem("使用地点", 40),
                    MenuItem("负责人", 50),
                    MenuItem("联系方式", 60),
                    MenuItem("备注", 70),
                    MenuItem("资产名", 10),
                    MenuItem("部门", 20),
                    MenuItem("编号", 30),
                    MenuItem("使用地点", 40),
                    MenuItem("负责人", 50),
                    MenuItem("联系方式", 60),
                    MenuItem("备注", 70),
                    MenuItem("资产名", 10),
                    MenuItem("部门", 20),
                    MenuItem("编号", 30),
                    MenuItem("使用地点", 40),
                    MenuItem("负责人", 50),
                    MenuItem("联系方式", 60),
                    MenuItem("备注", 70),
                    MenuItem("资产名", 10),
                    MenuItem("部门", 20),
                    MenuItem("编号", 30),
                    MenuItem("使用地点", 40),
                    MenuItem("负责人", 50),
                    MenuItem("联系方式", 60),
                    MenuItem("备注", 70),
                    MenuItem("资产名", 10),
                    MenuItem("部门", 20),
                    MenuItem("编号", 30),
                    MenuItem("使用地点", 40),
                    MenuItem("负责人", 50),
                    MenuItem("联系方式", 60),
                    MenuItem("备注", 70),
                ))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("提交")
                }
            }
        }

    }
}