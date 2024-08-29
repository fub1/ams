package com.crty.ams.asset.data.network.model

data class AssetForList(
    val id: Int,
    val name: String,
    val code: String,
    val department: String,

    val hasSubAssets: Boolean = false,
    val subAssets: List<AssetForList>? = null
)