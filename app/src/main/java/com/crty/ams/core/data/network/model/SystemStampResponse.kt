package com.crty.ams.core.data.network.model



import com.google.gson.annotations.SerializedName

data class SystemStampResponse(
    @SerializedName("version_") val version: String,
    @SerializedName("timestamp_") val timestamp: String
)