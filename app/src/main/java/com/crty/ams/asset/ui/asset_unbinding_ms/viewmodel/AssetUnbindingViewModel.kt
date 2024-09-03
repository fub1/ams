package com.crty.ams.asset.ui.asset_unbinding_ms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crty.ams.asset.data.network.model.AssetForList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AssetUnbindingViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {
    private val _assetId = MutableLiveData<Int>()
    val assetId: LiveData<Int> = _assetId

    fun setAssetId(id: Int){
        _assetId.value = id
    }

    private val _masterId = MutableLiveData<Int>()


    private val _selectedSubAssets = MutableStateFlow<MutableSet<AssetForList>>(mutableSetOf())
    val selectedSubAssets: StateFlow<Set<AssetForList>> = _selectedSubAssets



//    private val _assets = MutableStateFlow<List<AssetForList>>(listOf(
//        AssetForList(1, "主要资产 1", "PA1", "部门A", hasSubAssets = true, subAssets = listOf(
//            AssetForList(101, "子资产 1", "SA1", "部门A1"),
//            AssetForList(102, "子资产 2", "SA2", "部门A2"),
//            AssetForList(103, "子资产 3", "SA3", "部门A3")
//        )),
//        AssetForList(2, "主要资产 2", "PA2", "部门B"),
//        AssetForList(3, "主要资产 3", "PA3", "部门C", hasSubAssets = true, subAssets = listOf(
//            AssetForList(201, "子资产 4", "SA4", "部门C1"),
//            AssetForList(202, "子资产 5", "SA5", "部门C2")
//        )),
//        AssetForList(4, "主要资产 4", "PA4", "部门D"),
//        AssetForList(5, "主要资产 5", "PA5", "部门E", hasSubAssets = true, subAssets = listOf(
//            AssetForList(301, "子资产 6", "SA6", "部门E1"),
//            AssetForList(302, "子资产 7", "SA7", "部门E2"),
//            AssetForList(303, "子资产 8", "SA8", "部门E3"),
//            AssetForList(304, "子资产 9", "SA9", "部门E4")
//        ))
//    ))
//
//    val assets: StateFlow<List<AssetForList>> = _assets

    private val _assets = MutableStateFlow<List<AssetForList>>(emptyList())
    val assets: StateFlow<List<AssetForList>> = _assets

    init {

    }

    fun fetchAllAttributes(){
        parseAssets()
    }

    private fun parseAssets() {
        val rawAssets = listOf(
            AssetForList(id = 1, name = "主资产1", code = "A001", department = "部门A", parentId = 0),
            AssetForList(id = 2, name = "子资产1-1", code = "A002", department = "部门A", parentId = 1),
            AssetForList(id = 3, name = "子资产1-2", code = "A003", department = "部门A", parentId = 1),
            AssetForList(id = 4, name = "主资产2", code = "A004", department = "部门B", parentId = 0),
            AssetForList(id = 5, name = "子资产2-1", code = "A005", department = "部门B", parentId = 4)
        )

        val assetMap = mutableMapOf<Int, AssetForList>()

        rawAssets.forEach { asset ->
            assetMap[asset.id] = asset.copy(subAssetsForCheck = mutableListOf())
        }

        rawAssets.forEach { asset ->
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
    }
}