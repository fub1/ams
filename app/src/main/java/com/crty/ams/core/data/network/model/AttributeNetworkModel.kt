package com.crty.ams.core.data.network.model

import com.google.gson.annotations.SerializedName
import com.crty.ams.core.data.model.AttributeEntity

data class LocationResponse(
    val data: List<Location >?,
    val code: Int,
    val message: String
)

data class DepartmentResponse(
    val data: List< Department >?,
    val code: Int,
    val message: String
)

data class AssetCategoryResponse(
    val data: List< AssetCategory >?,
    val code: Int,
    val message: String
)



data class AssetCategoryRequest(
    @SerializedName("asset_category_code") val assetCategoryCode: String,
    @SerializedName("asset_category_desc") val assetCategoryDesc: String,
    @SerializedName("parent_id") val  parentId: Int
)

// 所有POST-请求的返回值
data class SubmitResponse(
    val data: List< Any > ,
    val code: Int,
    val message: String
)






class Location(
    @SerializedName("location_id") override val id: Int,
    @SerializedName("parent_id") override val parentId: Int,
    @SerializedName("location_desc") val description: String,

    ) : AttributeEntity(id,parentId, description, )


class Department(
    @SerializedName("department_id") override val id: Int,
    @SerializedName("parent_id") override val parentId: Int,
    @SerializedName("department_name")val description: String,
    ) : AttributeEntity(id,parentId, description )


class AssetCategory(
    @SerializedName("asset_category_id") override val id: Int,
    @SerializedName("parent_id") override val parentId: Int,
    @SerializedName("asset_category_desc")val description: String,
) : AttributeEntity(id,parentId, description )