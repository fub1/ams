package com.crty.ams.core.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crty.ams.core.ui.viewmodel.LoginViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberImagePainter
import com.crty.ams.core.ui.viewmodel.SystemSettingsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemSettingsScreen(navController: NavHostController, viewModel: SystemSettingsViewModel = hiltViewModel()) {


//    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//        Text(text = "系统设置", fontSize = 24.sp, modifier = Modifier.fillMaxWidth())
//    }

    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ProfileHeader(
                avatarResId = userProfile.avatarResId,
                nickname = userProfile.nickname,
                viewModel
            )
        }
    }
}

@Composable
fun ProfileHeader(avatarResId: Int, nickname: String, viewModel: SystemSettingsViewModel) {
    // 获取设备型号信息
    val deviceModel by viewModel.deviceModel.observeAsState("")
    // 获取示例设服务端地址
    val serverIpModel by viewModel.serverIpModel.observeAsState("")
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background) // 设置背景颜色
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(4.dp)) // 设置阴影
                .padding(8.dp), // 内部边距
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // 头像
            Image(
                painter = painterResource(id = avatarResId),
                contentDescription = "头像",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape) // 裁剪为圆形
                    .border(2.dp, MaterialTheme.colorScheme.onSecondary, CircleShape) // 添加圆形边框
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 昵称
            Column {
                Text(
                    text = nickname,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.Start, // 左对齐
            verticalArrangement = Arrangement.Top // 顶部对齐
        ) {
            // 标题
            Text(
                text = "设备型号",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth() // 确保标题宽度与父容器一致
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 显示设备型号信息
            Text(
                text = deviceModel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth() // 确保内容宽度与父容器一致
            )

            // 分割线
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // 为分割线添加一些上间距
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f), // 使用背景色的浅色调
                thickness = 1.dp // 设置分割线的厚度
            )

            // 标题
            Text(
                text = "服务端连接设置",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth() // 确保标题宽度与父容器一致
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 显示设备型号信息
            Text(
                text = serverIpModel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth() // 确保内容宽度与父容器一致
            )

            // 分割线
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp), // 为分割线添加一些上间距
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f), // 使用背景色的浅色调
                thickness = 1.dp // 设置分割线的厚度
            )
        }
    }
}