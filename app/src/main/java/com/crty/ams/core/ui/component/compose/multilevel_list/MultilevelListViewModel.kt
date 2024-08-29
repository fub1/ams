package com.crty.ams.core.ui.compose.multilevel_list

import androidx.lifecycle.ViewModel
import com.crty.ams.asset.data.network.model.AssetForList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
open class MultilevelListViewModel  @Inject constructor(

) : ViewModel() {

    private val _assets = MutableStateFlow<List<AssetForList>>(listOf(
        AssetForList(1, "主要资产 1", "PA1", "部门A", hasSubAssets = true, subAssets = listOf(
            AssetForList(101, "子资产 1", "SA1", "部门A1"),
            AssetForList(102, "子资产 2", "SA2", "部门A2"),
            AssetForList(103, "子资产 3", "SA3", "部门A3")
        )),
        AssetForList(2, "主要资产 2", "PA2", "部门B"),
        AssetForList(3, "主要资产 3", "PA3", "部门C", hasSubAssets = true, subAssets = listOf(
            AssetForList(201, "子资产 4", "SA4", "部门C1"),
            AssetForList(202, "子资产 5", "SA5", "部门C2")
        )),
        AssetForList(4, "主要资产 4", "PA4", "部门D"),
        AssetForList(5, "主要资产 5", "PA5", "部门E", hasSubAssets = true, subAssets = listOf(
            AssetForList(301, "子资产 6", "SA6", "部门E1"),
            AssetForList(302, "子资产 7", "SA7", "部门E2"),
            AssetForList(303, "子资产 8", "SA8", "部门E3"),
            AssetForList(304, "子资产 9", "SA9", "部门E4")
        ))
    ))

    val assets: StateFlow<List<AssetForList>> = _assets

    fun removeAsset(assetId: Int) {
        _assets.value = _assets.value.map { asset ->
            if (asset.id == assetId) {
                null
            } else if (asset.hasSubAssets) {
                asset.copy(subAssets = asset.subAssets?.filter { it.id != assetId })
            } else {
                asset
            }
        }.filterNotNull()
    }
}