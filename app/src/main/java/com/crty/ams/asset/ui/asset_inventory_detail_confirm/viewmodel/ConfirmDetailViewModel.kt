package com.crty.ams.asset.ui.asset_inventory_detail_confirm.viewmodel

import androidx.lifecycle.ViewModel
import com.crty.ams.asset.data.network.model.AssetForList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ConfirmDetailViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {

    private val _assets = MutableStateFlow(
        listOf(
            AssetForList(1, "资产1", "001", "部门A"),
            AssetForList(2, "资产2", "002", "部门B"),
            AssetForList(3, "资产3", "003", "部门C"),
            AssetForList(4, "资产4", "004", "部门D"),
            AssetForList(5, "资产5", "005", "部门E"),
            AssetForList(6, "资产6", "006", "部门F"),
            AssetForList(7, "资产7", "007", "部门G"),
            AssetForList(8, "资产8", "008", "部门H"),
            AssetForList(9, "资产9", "009", "部门I"),
            AssetForList(10, "资产10", "010", "部门J"),
            AssetForList(11, "资产11", "011", "部门K"),
            AssetForList(12, "资产12", "012", "部门L"),
            AssetForList(13, "资产13", "013", "部门M"),
            AssetForList(14, "资产14", "014", "部门N")
        )
    )
    val assets: StateFlow<List<AssetForList>> = _assets

    fun deleteAsset(assetId: Int) {
        _assets.value = _assets.value.filter { it.id != assetId }
    }
}