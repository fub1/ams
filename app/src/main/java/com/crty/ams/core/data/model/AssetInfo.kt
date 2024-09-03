package com.crty.ams.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetInfo(
    val asset_id: Int = 0,
    val asset_code: String,
    val asset_name: String,
    val asset_category: String,
    val asset_category_id: Int,
    val brand: String,
    val model: String,
    val sn: String = "",
    val supplier: String = "",
    val purchase_date: String = "",
    val price: String = "0",
    val remark: String = ""
)