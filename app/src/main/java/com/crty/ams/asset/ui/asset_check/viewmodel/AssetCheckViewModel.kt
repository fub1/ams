package com.crty.ams.asset.ui.asset_check.viewmodel

import androidx.lifecycle.ViewModel
import com.crty.ams.core.data.model.AssetInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AssetCheckViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {

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


    fun setScreenValue(){
        _asset.value = _asset.value.copy(asset_code = "code")
        _asset.value = _asset.value.copy(asset_name = "name")
        _asset.value = _asset.value.copy(asset_category = "category")
        _asset.value = _asset.value.copy(brand = "brand")
        _asset.value = _asset.value.copy(model = "model")
        _asset.value = _asset.value.copy(sn = "sn")
        _asset.value = _asset.value.copy(supplier = "supplier")
        _asset.value = _asset.value.copy(purchase_date = "pur")
        _asset.value = _asset.value.copy(price = "price")
    }

}