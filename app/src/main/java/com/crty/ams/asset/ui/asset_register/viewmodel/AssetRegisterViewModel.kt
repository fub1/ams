package com.crty.ams.asset.ui.asset_register.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.AssetInfo
import com.crty.ams.core.data.network.model.AssetRegistrationRequest
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
class AssetRegisterViewModel @Inject constructor(
    // Inject your repository or use case here
    private val coreRepository: CoreRepository
) : ViewModel() {
    private val _tid = MutableStateFlow("")
    val tid: StateFlow<String> = _tid

    private val _epc = MutableStateFlow("")
    val epc: StateFlow<String> = _epc

    private val _barcode = MutableStateFlow("")
    val barcode: StateFlow<String> = _barcode

    private val _asset = MutableStateFlow(
        AssetInfo(
            asset_code = "",
            asset_name = "",
            asset_category = "",
            asset_category_id = 0,
            brand = "",
            model = "",
            sn = "",
            supplier = "",
            purchase_date = "",
            price = "0",
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



    fun onAssetCodeChanged(value: String) {
        _asset.value = _asset.value.copy(asset_code = value)
    }

    fun onAssetNameChanged(value: String) {
        _asset.value = _asset.value.copy(asset_name = value)
    }

    fun onAssetCategoryChanged(value: String) {
        _asset.value = _asset.value.copy(asset_category = value)
    }

    fun onBrandChanged(value: String) {
        _asset.value = _asset.value.copy(brand = value)
    }

    fun onModelChanged(value: String) {
        _asset.value = _asset.value.copy(model = value)
    }

    fun onSnChanged(value: String) {
        _asset.value = _asset.value.copy(sn = value)
    }

    fun onSupplierChanged(value: String) {
        _asset.value = _asset.value.copy(supplier = value)
    }

    fun onPurchaseDateChanged(value: String) {
        _asset.value = _asset.value.copy(purchase_date = value)
    }

    fun onPriceChanged(value: String) {
        _asset.value = _asset.value.copy(price = value)
    }

    fun onRemarkChanged(value: String) {
        _asset.value = _asset.value.copy(remark = value)
    }


    //将下拉框选项的name和id与文本输入框绑定
    fun updateAssetCategoryId(name: String, id: Int) {
        _asset.value = _asset.value.copy(asset_category = name)
        _asset.value = _asset.value.copy(asset_category_id = id)
    }


    fun submit(){
        println("资产登记输入值: ${_asset.value.asset_code} ${_asset.value.asset_name} ${_asset.value.asset_category_id} ${_asset.value.brand} ${_asset.value.model} ${_asset.value.sn} ${_asset.value.supplier} ${_asset.value.purchase_date} ${_asset.value.price} ${_asset.value.remark} ")
//        performOperation()
//        println("epc:${_epc.value},barcode:${_barcode.value}")

        val a = AssetRegistrationRequest(
            assetCode = _asset.value.asset_code,
            assetName = _asset.value.asset_name,
            assetCategoryId = _asset.value.asset_category_id,
            brand = _asset.value.brand,
            model = _asset.value.model,
            sn = _asset.value.sn,
            supplier = _asset.value.supplier,
            purchaseDate = _asset.value.purchase_date,
            price = _asset.value.price.toDouble(),
            remark = _asset.value.remark,
            rfidCodeTid = _tid.value,
            rfidCodeEpc = _epc.value,
            barcode = _barcode.value
        )
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // 设置超时时间为5秒
                val result = withTimeoutOrNull(5000) {
//                    delay(10000)
                    coreRepository.submitAssetRegistration(a)
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
                        _asset.value = AssetInfo(
                            asset_code = "",
                            asset_name = "",
                            asset_category = "",
                            asset_category_id = 0,
                            brand = "",
                            model = "",
                            sn = "",
                            supplier = "",
                            purchase_date = "",
                            price = "0",
                            remark = ""
                        )
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

    fun setEpcAndBarcode(tid: String, epc: String, barcode: String){
        _tid.value = tid
        _epc.value = epc
        _barcode.value = barcode
    }

}