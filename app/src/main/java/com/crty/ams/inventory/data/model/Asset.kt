package com.crty.ams.inventory.data.model

data class Asset(
    val id: Int,
    val name: String,
    val code: String,
    val department: String,

    val hasSubAssets: Boolean = false,
    val subAssets: List<Asset>? = null
)