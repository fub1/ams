package com.crty.ams.asset.data.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AssetForGroup(
    var id: Int,
    var name: String,
    var code: String,
    var department: String,
    var parentId: Int
)