package com.crty.ams.inventory.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.crty.ams.inventory.data.model.Asset
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
            Asset(1, "资产1", "001", "部门A"),
            Asset(2, "资产2", "002", "部门B"),
            Asset(3, "资产3", "003", "部门C"),
            Asset(4, "资产4", "004", "部门D"),
            Asset(5, "资产5", "005", "部门E"),
            Asset(6, "资产6", "006", "部门F"),
            Asset(7, "资产7", "007", "部门G"),
            Asset(8, "资产8", "008", "部门H"),
            Asset(9, "资产9", "009", "部门I"),
            Asset(10, "资产10", "010", "部门J"),
            Asset(11, "资产11", "011", "部门K"),
            Asset(12, "资产12", "012", "部门L"),
            Asset(13, "资产13", "013", "部门M"),
            Asset(14, "资产14", "014", "部门N")
        )
    )
    val assets: StateFlow<List<Asset>> = _assets

    fun deleteAsset(assetId: Int) {
        _assets.value = _assets.value.filter { it.id != assetId }
    }
}