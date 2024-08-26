package com.crty.ams.asset.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.crty.ams.core.data.model.Asset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AssetChangeSingleViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {
    private val _asset = MutableStateFlow(
        Asset(
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
    val asset: StateFlow<Asset> = _asset.asStateFlow()

    fun setScreenValue(){
        _asset.value = _asset.value.copy(asset_code = "code")
        _asset.value = _asset.value.copy(asset_name = "name")
        _asset.value = _asset.value.copy(asset_category = "category")
        _asset.value = _asset.value.copy(asset_category_id = 1)
        _asset.value = _asset.value.copy(brand = "brand")
        _asset.value = _asset.value.copy(model = "model")
        _asset.value = _asset.value.copy(sn = "sn")
        _asset.value = _asset.value.copy(supplier = "supplier")
        _asset.value = _asset.value.copy(purchase_date = "pur")
        _asset.value = _asset.value.copy(price = "price")
    }


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

}