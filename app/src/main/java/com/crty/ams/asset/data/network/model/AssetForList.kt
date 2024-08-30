package com.crty.ams.asset.data.network.model

data class AssetForList(
    val id: Int,
    val name: String,
    val code: String,
    val department: String,
    val parentId: Int? = 0,

    var hasSubAssets: Boolean = false,
    val subAssets: List<AssetForList>? = null,//用于盘点单明细等（删除功能）
    val subAssetsForCheck: MutableList<AssetForList> = mutableListOf() // 二维结构 用于资产成组解绑等（勾选功能）
)