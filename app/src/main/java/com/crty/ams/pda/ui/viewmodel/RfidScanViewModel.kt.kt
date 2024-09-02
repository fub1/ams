package com.crty.ams.pda.ui.viewmodel


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crty.ams.asset.ui.asset_inventory_list.viewmodel.InventoryListViewModel
import com.crty.ams.pda.utils.rfid.RfidScanManager

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RfidScanViewModel @Inject constructor(
    private val rfidManager: RfidScanManager // Inject RfidScanManager
) : ViewModel() {

    // ui state flow
    private val _rfidPower = MutableStateFlow(30)
    val rfidPower: Flow<Int> = _rfidPower


    fun rfidInit(context: Context) {
        // 初始化 RFID
        viewModelScope.launch {
            rfidManager.initRfid(context)
        }

    }

    // 防抖动-2任务标识变量
    private var debounceJob: Job? = null

    // 例如，设置 RFID 功率
    fun onSetRfidPower(targetPower: Int) {
        // 防抖动-1
        debounceJob?.cancel()
        // 放置抖动-3 更新标识
        debounceJob = CoroutineScope(Dispatchers.Main).launch {
            delay(200) // 设置防抖时间（300毫秒）
            if (rfidManager.setRfidPower(targetPower)) {
                _rfidPower.value = targetPower
            }
        }
    }




    fun rfidDestroy() {
        // 销毁 RFID
        viewModelScope.launch {
            rfidManager.destroyRfid()
        }
    }

    fun inventoryStart() {
        // 开始盘点
        viewModelScope.launch {
            Log.i("RfidScanViewModel", "inventoryStart")
            rfidManager.startInventoryTag()
        }
    }

    fun inventoryStop() {
        // 停止盘点
        viewModelScope.launch {
            Log.i("RfidScanViewModel", "inventoryStop")
            rfidManager.stopInventoryTag()
        }
    }



}