package com.crty.ams.asset.ui.asset_register.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.AssetInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetRegisterViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {
//    private val _asset_code = MutableStateFlow("")
//    val asset_code: StateFlow<String> = _asset_code.asStateFlow()
//
//    private val _asset_name = MutableStateFlow("")
//    val asset_name: StateFlow<String> = _asset_name.asStateFlow()
//
//
//    private val _asset_category = MutableStateFlow("")
//    val asset_category: StateFlow<String> = _asset_category.asStateFlow()
//
//    private val _asset_category_id = MutableStateFlow<Int?>(null)
//    val asset_category_id: StateFlow<Int?> = _asset_category_id.asStateFlow()
//
//    private val _brand = MutableStateFlow("")
//    val brand: StateFlow<String> = _brand.asStateFlow()
//
//    private val _model = MutableStateFlow("")
//    val model: StateFlow<String> = _model.asStateFlow()
//
//    private val _sn = MutableStateFlow("")
//    val sn: StateFlow<String> = _sn.asStateFlow()
//
//    private val _supplier = MutableStateFlow("")
//    val supplier: StateFlow<String> = _supplier.asStateFlow()
//
//    private val _purchase_date = MutableStateFlow("")
//    val purchase_date: StateFlow<String> = _purchase_date.asStateFlow()
//
//    private val _price = MutableStateFlow("")
//    val price: StateFlow<String> = _price.asStateFlow()

    private val _asset = MutableStateFlow(
        AssetInfo(
            asset_code = "",
            asset_name = "",
            asset_category = "",
            asset_category_id = null,
            brand = "",
            model = "",
            sn = "",
            supplier = "",
            purchase_date = "",
            price = ""
        )
    )
    val asset: StateFlow<AssetInfo> = _asset.asStateFlow()


    // 控制动画和文字显示的状态
    val showSuccessPopup = mutableStateOf(false)



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


    //将下拉框选项的name和id与文本输入框绑定
    fun updateAssetCategoryId(name: String, id: Int) {
        _asset.value = _asset.value.copy(asset_category = name)
        _asset.value = _asset.value.copy(asset_category_id = id)
    }


    fun submit(){
        println("资产登记输入值: ${_asset.value.asset_code} ${_asset.value.asset_name} ${_asset.value.asset_category_id} ${_asset.value.brand} ${_asset.value.model} ${_asset.value.sn} ${_asset.value.supplier} ${_asset.value.purchase_date} ${_asset.value.price} ")
        performOperation()

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

}