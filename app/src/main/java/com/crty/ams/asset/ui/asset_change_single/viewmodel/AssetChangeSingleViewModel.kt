package com.crty.ams.asset.ui.asset_change_single.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crty.ams.core.data.model.AssetInfo
import com.crty.ams.core.ui.compose.picker.AttributeEntity
import com.crty.ams.core.ui.compose.picker.AttributeViewModel.AttributeViewMode
import com.crty.ams.core.ui.compose.picker.AttributeViewModel.AttributeViewState
import com.crty.ams.core.ui.compose.picker.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetChangeSingleViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {
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
            price = "",
            remark = ""
        )
    )
    val asset: StateFlow<AssetInfo> = _asset.asStateFlow()


    // 控制动画和文字显示的状态
    val showSuccessPopup = mutableStateOf(false)

    init {
//        _asset.value = _asset.value.copy(asset_code = "code")
//        _asset.value = _asset.value.copy(asset_name = "name")
//        _asset.value = _asset.value.copy(asset_category = "category")
//        _asset.value = _asset.value.copy(asset_category_id = 1)
//        _asset.value = _asset.value.copy(brand = "brand")
//        _asset.value = _asset.value.copy(model = "model")
//        _asset.value = _asset.value.copy(sn = "sn")
//        _asset.value = _asset.value.copy(supplier = "supplier")
//        _asset.value = _asset.value.copy(purchase_date = "pur")
//        _asset.value = _asset.value.copy(price = "price")
        _asset.value = AssetInfo("code", "name", "category", 1, "brand", "model", "sn", "supplier", "pur", "price", "remark")
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