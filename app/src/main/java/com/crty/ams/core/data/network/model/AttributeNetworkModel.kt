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
    val data: Any ,
    val code: Int,
    val message: String
)

// 资产登记请求
data class AssetRegistrationRequest(
    @SerializedName("asset_code") val assetCode: String,
    @SerializedName("asset_name") val assetName: String,
    @SerializedName("asset_category_id") val assetCategoryId: Int,
    @SerializedName("brand") val brand: String,
    @SerializedName("model") val model: String,
    @SerializedName("sn") val sn: String? = null,
    @SerializedName("supplier") val supplier: String? = null,
    @SerializedName("purchase_date") val purchaseDate: String? = null,
    @SerializedName("price") val price: Double? = null,
    @SerializedName("remark") val remark: String? = null,
    @SerializedName("rfid_code_tid") val rfidCodeTid: String? = null,
    @SerializedName("rfid_code_epc") val rfidCodeEpc: String,
    @SerializedName("barcode") val barcode: String? = null
)

// 主从资产解绑请求
data class AssetUnbindingMSRequest(
    @SerializedName("asset_ids") val ids: List<Int>,
    @SerializedName("flag") val flag: Int,
)

//单个/批量资产变更请求
data class AssetChangeRequest(
    @SerializedName("asset_id") val ids: List<Int>,
    @SerializedName("asset_category_id") val categoryId: Int,
    @SerializedName("asset_name") val name: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("model") val model: String,
    @SerializedName("price") val price: Double,
    @SerializedName("purchase_date") val date: String,
    @SerializedName("sn") val sn: String,
    @SerializedName("supplier") val supplier: String,
    @SerializedName("remark") val remark: String,
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