package com.crty.ams.asset.ui.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdUnits
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Bathroom
import androidx.compose.material.icons.filled.BlurLinear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsOff
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.OilBarrel
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.YoutubeSearchedFor
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList


data class MenuItem(val id: Int, val title: String, val icon: ImageVector)

@HiltViewModel
class AssetViewModel @Inject constructor(
    // Inject your repository or use case here
) : ViewModel() {

    private val _menuItems = mutableStateListOf(
        MenuItem(1, "资产登记", Icons.Filled.Home),
        MenuItem(2, "资产调拨", Icons.Filled.DirectionsOff),
        MenuItem(3, "资产领用", Icons.Filled.Settings),
        MenuItem(4, "资产查找", Icons.Filled.Favorite),
        MenuItem(5, "资产盘点", Icons.Filled.AdUnits),
        MenuItem(6, "资产发出", Icons.Filled.AddCircle),
        MenuItem(7, "资产组管理（主从）", Icons.Filled.Anchor),
        MenuItem(8, "资产组信息维护", Icons.Filled.Bathroom),
        MenuItem(9, "资产查询", Icons.Filled.BlurLinear),
        MenuItem(10, "标签绑定", Icons.Filled.DateRange),
        MenuItem(11, "标签解绑", Icons.Filled.YoutubeSearchedFor),
        MenuItem(12, "资产报废", Icons.Filled.OilBarrel)
    )

    val menuItems: SnapshotStateList<MenuItem> get() = _menuItems
}