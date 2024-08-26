package com.crty.ams.core.ui.compose.multilevel_list

import androidx.lifecycle.ViewModel
import com.crty.ams.inventory.data.model.Asset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
open class MultilevelListViewModel  @Inject constructor(

) : ViewModel() {

    private val _assets = MutableStateFlow<List<Asset>>(listOf(
        Asset(1, "主要资产 1", "PA1", "部门A", hasSubAssets = true, subAssets = listOf(
            Asset(101, "子资产 1", "SA1", "部门A1"),
            Asset(102, "子资产 2", "SA2", "部门A2"),
            Asset(103, "子资产 3", "SA3", "部门A3")
        )),
        Asset(2, "主要资产 2", "PA2", "部门B"),
        Asset(3, "主要资产 3", "PA3", "部门C", hasSubAssets = true, subAssets = listOf(
            Asset(201, "子资产 4", "SA4", "部门C1"),
            Asset(202, "子资产 5", "SA5", "部门C2")
        )),
        Asset(4, "主要资产 4", "PA4", "部门D"),
        Asset(5, "主要资产 5", "PA5", "部门E", hasSubAssets = true, subAssets = listOf(
            Asset(301, "子资产 6", "SA6", "部门E1"),
            Asset(302, "子资产 7", "SA7", "部门E2"),
            Asset(303, "子资产 8", "SA8", "部门E3"),
            Asset(304, "子资产 9", "SA9", "部门E4")
        ))
    ))

    val assets: StateFlow<List<Asset>> = _assets

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