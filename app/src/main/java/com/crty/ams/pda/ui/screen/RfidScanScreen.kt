package com.crty.ams.pda.ui.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.crty.ams.pda.ui.component.SetRfidPowerSkeleton
import com.crty.ams.pda.ui.viewmodel.RfidScanViewModel
import com.crty.ams.pda.utils.keyevent.ScanKeyEventReceiver

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RfidScanScreen(
    navController: NavHostController,
    viewModel: RfidScanViewModel = hiltViewModel()) {
    val rfidPower by viewModel.rfidPower.collectAsState(initial = 30)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var scanKeyEventReceiver by remember { mutableStateOf<ScanKeyEventReceiver?>(null) }

    // 定义 LifecycleEventObserver 来捕获生命周期事件
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    Log.i("Seuic-Key tester", "ON_START")
                    scanKeyEventReceiver?.unregisterCall()

                    // 初始化模块
                    viewModel.rfidInit(context)


                    // 重新注册 remember 中的实例，并设置自定义的按键处理逻辑
                    scanKeyEventReceiver = ScanKeyEventReceiver().apply {
                        onKeyDownAction = { keyCode ->
                            // 自定义 onKeyDown 行为
                            Log.i("Seuic-Key tester", "Custom onKeyDown: keyCode=$keyCode")
                            // 可以在此处加入更多自定义逻辑
                            viewModel.inventoryStart()

                        }
                        onKeyUpAction = { keyCode ->
                            // 自定义 onKeyUp 行为
                            Log.i("Seuic-Key tester", "Custom onKeyUp: keyCode=$keyCode")
                            // 可以在此处加入更多自定义逻辑
                            viewModel.inventoryStop()
                        }
                        registerCall()
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    Log.i("Seuic-Key tester", "ON_STOP")
                    viewModel.rfidDestroy()
                    scanKeyEventReceiver?.unregisterCall()
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            Log.i("Seuic-Key tester", "onDispose")
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.rfidDestroy()
            scanKeyEventReceiver?.unregisterCall()
            scanKeyEventReceiver = null
        }
    }





    Column {
        SetRfidPowerSkeleton(
            power = rfidPower,
            onPowerChange = { newValue -> viewModel.onSetRfidPower(newValue) }
        )

        Text(text = "logcat will show key event")
        Text(text = "logcat 查看日志")
        Text(text = "Seuic 按键不广播，通过SDK-Singleton调用")
        Text(text = "监听注册状态和Screen生命周期绑定 - logcat 查看日志")
        Text(text = "KEY_CODE_SIDESCANKEY（左侧扫键） = 248")
        Text(text = "KEY_CODE_SIDESCANKEY（右侧扫键） = 249")
        Text(text = "KEY_CODE_MAINSCANKEY（扳机键） = 250")
    }
}