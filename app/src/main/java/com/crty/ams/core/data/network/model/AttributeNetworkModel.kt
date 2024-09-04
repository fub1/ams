package com.crty.ams.core.data.network.model

import com.google.gson.annotations.SerializedName
import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.data.model.UserAttributeEntity

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

data class PersonResponse(
    val data: PersonData,
    val code: Int,
    val message: String
)

class PersonData(
    val list: List< Person >?
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
    @SerializedName("parent_asset_id") var parentId: Int?,
    @SerializedName("asset_id") var ids: List<Int>?,
    @SerializedName("asset_category_id") var categoryId: Int?,
    @SerializedName("asset_name") var name: String?,
    @SerializedName("brand") var brand: String?,
    @SerializedName("model") var model: String?,
    @SerializedName("price") var price: Double?,
    @SerializedName("purchase_date") var date: String?,
    @SerializedName("sn") var sn: String?,
    @SerializedName("supplier") var supplier: String?,
    @SerializedName("remark") var remark: String?,
)

//资产调拨
data class AssetAllocationRequest(
    @SerializedName("asset_id") var assetId: List<Int>,
    @SerializedName("after_department_id") var afterDepartmentId: Int?,
    @SerializedName("after_location_id") var afterLocationId: Int?,
    @SerializedName("after_personnel_id") var afterPersonnelId: Int?,
    @SerializedName("remark") var remark: String?,
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

class Person(
    @SerializedName("personnel_id") override val id: Int,
    @SerializedName("personnel_name") override val name: String,
    @SerializedName("department_id") override val departmentId: Int,
    @SerializedName("department_name") override val departmentName: String,
) : UserAttributeEntity(id, name, departmentId, departmentName)
//class Person(
//    @SerializedName("personnel_id") override val id: Int,
//    @SerializedName("department_id") override val parentId: Int,
//    @SerializedName("personnel_name")val description: String,
//) : AttributeEntity(id,parentId, description )