package com.crty.ams.asset.ui.asset_unbinding_ms.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.asset.data.network.model.AssetForGroup
import com.crty.ams.asset.data.network.model.AssetForList
import com.crty.ams.core.data.model.AssetInfo
import com.crty.ams.core.data.network.model.AssetUnbindingMSRequest
import com.crty.ams.core.data.repository.CoreRepository
import com.crty.ams.core.ui.compose.picker.AttributeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class AssetUnbindingViewModel @Inject constructor(
    // Inject your repository or use case here
    private val coreRepository: CoreRepository
) : ViewModel() {
    // 控制动画和文字显示的状态
    val showSuccessPopup = mutableStateOf(false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _isTimeout  = MutableStateFlow(false)
    val isTimeout: StateFlow<Boolean> = _isTimeout
    private val _isFailed  = MutableStateFlow(false)
    val isFailed: StateFlow<Boolean> = _isFailed
    private val _failedMessage  = MutableStateFlow("")
    val failedMessage: StateFlow<String> = _failedMessage
    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _masterId = MutableLiveData<Int>()


    private val _selectedSubAssets = MutableStateFlow<MutableSet<AssetForList>>(mutableSetOf())
    val selectedSubAssets: StateFlow<Set<AssetForList>> = _selectedSubAssets


    private val _assets = MutableStateFlow<List<AssetForList>>(emptyList())
    val assets: StateFlow<List<AssetForList>> = _assets


    fun fetchAllAttributes(assets: List<AssetForGroup>){
        val newAttributesList = mutableListOf<AssetForList>()
        assets.forEach { attributeData ->
            // 假设 attributeData 是你想要转换为 AttributeEntity 的数据类型
            val attributeEntity = AssetForList(
                id = attributeData.id,
                name = attributeData.name,
                code = attributeData.code,
                department = attributeData.department,
                parentId = attributeData.parentId
            )
            // 将新创建的 AttributeEntity 对象添加到列表中
            newAttributesList.add(attributeEntity)
        }
        parseAssets(newAttributesList)
    }

    private fun parseAssets(assets: List<AssetForList>) {

        val assetMap = mutableMapOf<Int, AssetForList>()

        assets.forEach { asset ->
            assetMap[asset.id] = asset.copy(subAssetsForCheck = mutableListOf())
        }

        assets.forEach { asset ->
            asset.parentId.takeIf { it != 0 }?.let { parentId ->
                assetMap[parentId]?.hasSubAssets = true
                assetMap[parentId]?.subAssetsForCheck?.add(asset)

                _masterId.value = assetMap[parentId]?.id //获取主资产的id
            }
        }

        _assets.value = assetMap.values.filter { it.parentId == 0 }

    }


    fun toggleSubAssetSelection(subAsset: AssetForList, isChecked: Boolean) {
        val currentSelection = _selectedSubAssets.value.toMutableSet()
        if (isChecked) {
            currentSelection.add(subAsset)
        } else {
            currentSelection.remove(subAsset)
        }
        _selectedSubAssets.value = currentSelection
    }


    fun unbindAll(){
        println("全部解绑：${_masterId.value}")
        val newAttributesList = mutableListOf<Int>()
        _masterId.value?.let { newAttributesList.add(it) }
        val a = AssetUnbindingMSRequest(
            newAttributesList,
            0
        )
        sendRequest(a)
    }
    fun unbindPart(ids: List<Int>){
        ids.forEach { id->
            println("部分解绑：$id")
        }
        val a = AssetUnbindingMSRequest(
            ids,
            1
        )
    }

    private fun sendRequest(assetUnbindingMSRequest: AssetUnbindingMSRequest){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // 设置超时时间为5秒
                val result = withTimeoutOrNull(22000) {
//                    delay(10000)
                    coreRepository.submitAssetUnbindingMS(assetUnbindingMSRequest)
                }
                _isLoading.value = false

                if (result == null) {
                    // 超时逻辑处理
                    // 显示超时提示信息
                    _isTimeout.value = true
                } else {
                    // 正常处理
                    if (result.getOrNull()?.code == 0){
                        performOperation()

                        //处理跳转

                    }else if (result.getOrNull()?.code == 1){
                        _isFailed.value = true
                        _failedMessage.value = result.getOrNull()?.message.toString()
                    }else if (result.getOrNull()?.code == -1){
                        //退出登录

                    }

                }
            } catch (e: Exception) {
                // 处理其他异常
                _isLoading.value = false
                _isError.value = true
            }
        }
    }

    // 模拟执行操作的方法
    fun performOperation() {
        viewModelScope.launch {
            // 显示弹窗
            showSuccessPopup.value = true
            // 延迟2秒后隐藏弹窗
            delay(2000)
            showSuccessPopup.value = false
        }
    }

    fun dismissTimeoutDialog() {
        _isTimeout.value = false
    }
    fun dismissFieldDialog() {
        _isFailed.value = false
    }
    fun dismissErrorDialog() {
        _isError.value = false
    }
}