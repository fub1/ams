package com.crty.ams.asset.ui.asset_change_single.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.AssetInfo
import com.crty.ams.core.data.network.model.AssetChangeRequest
import com.crty.ams.core.data.repository.CoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class AssetChangeSingleViewModel @Inject constructor(
    // Inject your repository or use case here
    private val coreRepository: CoreRepository
) : ViewModel() {
    private val _asset = MutableStateFlow(
        AssetInfo(
            asset_id = 0,
            asset_code = "",
            asset_name = "",
            asset_category = "",
            asset_category_id = 0,
            brand = "",
            model = "",
            sn = "",
            supplier = "",
            purchase_date = "",
            price = "",
            remark = ""
        )
    )
    val asset: StateFlow<AssetInfo> = _asset.asStateFlow()


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

    init {

//        _asset.value = AssetInfo("code", "name", "category", 1, "brand", "model", "sn", "supplier", "pur", "price", "remark")
    }

    fun fetchData(assetInfo: AssetInfo){
        _asset.value = assetInfo
    }

    fun updateAssetField(field: (AssetInfo) -> AssetInfo) {
        _asset.value = field(_asset.value)
    }

    //将下拉框选项的name和id与文本输入框绑定
    fun updateAssetCategoryId(name: String, id: Int) {
        _asset.value = _asset.value.copy(asset_category = name)
        _asset.value = _asset.value.copy(asset_category_id = id)
    }


    fun submit(){
        println("资产变更输入值: ${_asset.value.asset_code} ${_asset.value.asset_name} ${_asset.value.asset_category_id} ${_asset.value.brand} ${_asset.value.model} ${_asset.value.sn} ${_asset.value.supplier} ${_asset.value.purchase_date} ${_asset.value.price} ${_asset.value.remark} ")
        val newAttributesList = mutableListOf<Int>()
        newAttributesList.add(_asset.value.asset_id)
        val a = AssetChangeRequest(
            parentId = null,
            ids = newAttributesList,
            categoryId = _asset.value.asset_category_id,
            name = _asset.value.asset_name,
            brand = _asset.value.brand,
            model = _asset.value.model,
            price = _asset.value.price.toDouble(),
            date = _asset.value.purchase_date,
            sn = _asset.value.sn,
            supplier = _asset.value.supplier,
            remark = _asset.value.remark
        )
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // 设置超时时间为5秒
                val result = withTimeoutOrNull(22000) {
//                    delay(10000)
                    coreRepository.submitAssetChange(a)
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