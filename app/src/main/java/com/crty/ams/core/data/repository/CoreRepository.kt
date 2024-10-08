package com.crty.ams.core.data.repository

// CoreRepository.kt


import android.util.Log
import android.util.Printer
import com.crty.ams.core.data.datastore.di.AppParameterDataStore
import com.crty.ams.core.data.network.api.CoreApiService
import com.crty.ams.core.data.network.model.AssetAllocationRequest
import com.crty.ams.core.data.network.model.AssetCategory
import com.crty.ams.core.data.network.model.AssetCategoryRequest
import com.crty.ams.core.data.network.model.AssetCategoryResponse
import com.crty.ams.core.data.network.model.AssetChangeRequest
import com.crty.ams.core.data.network.model.AssetRegistrationRequest
import com.crty.ams.core.data.network.model.AssetUnbindingMSRequest
import com.crty.ams.core.data.network.model.Department
import com.crty.ams.core.data.network.model.DepartmentResponse
import com.crty.ams.core.data.network.model.Location
import com.crty.ams.core.data.network.model.LocationResponse
import com.crty.ams.core.data.network.model.LoginRequest
import com.crty.ams.core.data.network.model.LoginResponse
import com.crty.ams.core.data.network.model.LoginResult
import com.crty.ams.core.data.network.model.Person
import com.crty.ams.core.data.network.model.PersonData
import com.crty.ams.core.data.network.model.PersonResponse
import com.crty.ams.core.data.network.model.SubmitResponse
import com.crty.ams.core.data.network.model.SystemStampResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CoreRepository @Inject constructor(
    private val coreApiService: CoreApiService,
    private val appParameterRepository: AppParameterRepository
) {
    //TODO 服务器不可达异常处理
    suspend fun getSystemStamp(): Result<SystemStampResponse> {

        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/login/Stamp"
            Log.d("CoreRepository", "Fetching system stamp from: $fullUrl")
            val response = coreApiService.getSystemStamp(fullUrl)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API request failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // TODO 接受登录参数

    suspend fun login(): Result<LoginResult> {
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            // 完整URL
            val fullUrl = "${url}:${port}/api/login"
            Log.d("CoreRepository", "Fetching system stamp from: $fullUrl")
            // 执行请求
            val response =
                coreApiService.login(fullUrl, 1, LoginRequest("admin", "123456", "UM5230301811"))
            Log.d("CoreRepository", "Response code: ${response.code()}")
            Log.d("CoreRepository", "Response code: ${response.body()?.code}")
            if (response.body()?.code == 0) {
                // 请求成功
                val loginToken = response.body()?.data?.access_token ?: ""
                Log.d("GetLoginToken", "Response token: $loginToken")
                // save token to proto
                appParameterRepository.updateAppParameterToken(loginToken)

                return Result.success(LoginResult(0, "Login success"))
                // save token to proto


            } else {
                // 请求失败msg获取
                Log.d("CoreRepository", "Login failed: ${response.body()?.message}")

                val msg = response.body()?.message ?: "Unknown error"
                return Result.success(LoginResult(1, msg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(departmentId: Int): Result<PersonResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = if (departmentId == 0) {
                "${url}:${port}/api/Basic/person"
            } else {
                "${url}:${port}/api/Basic/person?department_id=$departmentId"
            }
            Log.d("CoreRepository", "Fetching person from: $fullUrl with token: $token")
            val response = coreApiService.getPerson(fullUrl, 1, token)

            Log.d("CoreRepository", "Response code: ${response.code()}")
            Log.d("CoreRepository", "get dprt count: ${response.body()?.data?.list?.size}")

            // Part1- Token过期
            // 返回类code-1，vm中处理登出
            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(PersonResponse(PersonData(emptyList()), -1, "Token expired"))
            }
            // Part2- 数据返回
            if (response.isSuccessful) {
                val feedbackMsg = response.body()?.message ?: "Unknown error"
                val persons: MutableList<Person> = mutableListOf()
                response.body()!!.data.list?.forEach { person ->
                    persons.add(person)
                    Log.d(
                        "CoreRepository",
                        "Add-ID${person.id} Department is ${person.name}, "
                    )
                }

                val departmentResponse = PersonResponse(
                    data = PersonData(persons),
                    code = 0,
                    message = feedbackMsg
                )
                Log.d("CoreRepository", "getDepartment count: ${departmentResponse.message} ")
                return Result.success(departmentResponse)
            } else {
                Log.d("CoreRepository", "getDepartment failed: ${response.body()?.message}")
                return Result.success(PersonResponse(PersonData(emptyList()), 1, "unknown error"))

            }

            // Part3- 异常返回
        } catch (e: Exception) {
            return Result.success(PersonResponse(PersonData(emptyList()), 1, "unknown error"))
        }
    }


    suspend fun getDepartment(): Result<DepartmentResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/Basic/department"
            Log.d("CoreRepository", "Fetching department from: $fullUrl with token: $token")
            val response = coreApiService.getDepartment(fullUrl, 1, token)

            Log.d("CoreRepository", "Response code: ${response.code()}")
            Log.d("CoreRepository", "get dprt count: ${response.body()?.data?.size}")

            // Part1- Token过期
            // 返回类code-1，vm中处理登出
            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(DepartmentResponse(emptyList(), -1, "Token expired"))
            }
            // Part2- 数据返回
            if (response.isSuccessful) {
                val feedbackMsg = response.body()?.message ?: "Unknown error"
                val departments: MutableList<Department> = mutableListOf()
                response.body()!!.data?.forEach { department ->
                    departments.add(department)
                    Log.d(
                        "CoreRepository",
                        "Add-ID${department.id} Department is ${department.description}, "
                    )
                }

                val departmentResponse = DepartmentResponse(
                    data = departments,
                    code = 0,
                    message = feedbackMsg
                )
                Log.d("CoreRepository", "getDepartment count: ${departmentResponse.message} ")
                return Result.success(departmentResponse)
            } else {
                Log.d("CoreRepository", "getDepartment failed: ${response.body()?.message}")
                return Result.success(DepartmentResponse(emptyList(), 1, "unknown error"))

            }

            // Part3- 异常返回
        } catch (e: Exception) {
            return Result.success(DepartmentResponse(emptyList(), 1, "unknown error"))
        }
    }

    // DoneTODO 定义接口超时
    // TODO 定义错误处理
    suspend fun getLocations(): Result<LocationResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/Basic/location"
            Log.d("CoreRepository", "Fetching location from: $fullUrl with token: $token")
            val response = coreApiService.getLocation(fullUrl, 1, token)

            Log.d("CoreRepository", "Response code: ${response.code()}")
            Log.d("CoreRepository", "get dprt count: ${response.body()?.data?.size}")

            // Part1- Token过期
            // 返回类code-1，vm中处理登出
            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(LocationResponse(emptyList(), -1, "Token expired"))
            }
            // Part2- 数据返回
            if (response.isSuccessful) {
                val feedbackMsg = response.body()?.message ?: "Unknown error"
                val locations: MutableList<Location> = mutableListOf()
                response.body()!!.data?.forEach { location ->
                    locations.add(location)
                    Log.d(
                        "CoreRepository",
                        "Add-ID${location.id} Location is ${location.description}, "
                    )
                }

                val locationResponse = LocationResponse(
                    data = locations,
                    code = 0,
                    message = feedbackMsg
                )
                Log.d("CoreRepository", "getLocation count: ${locationResponse.message} ")
                return Result.success(locationResponse)
            } else {
                Log.d("CoreRepository", "getLocation failed: ${response.body()?.message}")
                return Result.success(LocationResponse(emptyList(), 1, "unknown error"))

            }

            // Part3- 异常返回
        } catch (e: Exception) {
            return Result.success(LocationResponse(emptyList(), 1, "unknown error"))
        }
    }

    //  AssetCategoryResponse-取资产分类
    suspend fun getAssetCategory(): Result<AssetCategoryResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/Basic/asset_category"
            Log.d("CoreRepository", "Fetching asset_category from: $fullUrl with token: $token")
            val response = coreApiService.getAssetCategory(fullUrl, 1, token)

            Log.d("CoreRepository", "Response code: ${response.body()?.code}")
            Log.d("CoreRepository", "get asset_category count: ${response.body()?.data?.size}")

            // Part1- Token过期
            // 返回类code-1，vm中处理登出
            // Part2- 数据返回
            if (response.isSuccessful) {
                val feedbackMsg = response.body()?.message ?: "Unknown error"
                val assetCategorys: MutableList<AssetCategory> = mutableListOf()
                response.body()!!.data?.forEach { assetCategory ->
                    assetCategorys.add(assetCategory)
                    Log.d(
                        "CoreRepository",
                        "Add-ID${assetCategory.id} Location is ${assetCategory.description}, "
                    )
                }

                val toResponse = AssetCategoryResponse(
                    data = assetCategorys,
                    code = 0,
                    message = feedbackMsg
                )
                Log.d("CoreRepository", "getLocation count: ${toResponse.message} ")
                return Result.success(toResponse)


            } else {
                return Result.success(AssetCategoryResponse(emptyList(), 1, "unknown error"))
            }
        } catch (e: Exception) {
            return Result.success(AssetCategoryResponse(emptyList(), 1, "unknown error"))
        }
    }

//    //  AssetCategoryResponse-取使用位置
//    suspend fun getLocation(): Result<LocationResponse> {
//        val token: String = "bearer " + appParameterRepository.getToken()
//        val url: String = appParameterRepository.getBaseUrl()
//        val port: Int = appParameterRepository.getBasePort()
//        return try {
//            val fullUrl = "${url}:${port}/api/basic/location"
//            Log.d("CoreRepository", "Fetching location from: $fullUrl with token: $token")
//            val response = coreApiService.getLocation(fullUrl, 1, token)
//
//            Log.d("CoreRepository", "Response code: ${response.body()?.code}")
//            Log.d("CoreRepository", "get asset_category count: ${response.body()?.data?.size}")
//
//            // Part1- Token过期
//            // 返回类code-1，vm中处理登出
//            // Part2- 数据返回
//            if (response.isSuccessful) {
//                val feedbackMsg = response.body()?.message ?: "Unknown error"
//                val assetCategorys: MutableList<AssetCategory> = mutableListOf()
//                response.body()!!.data?.forEach { assetCategory ->
//                    assetCategorys.add(assetCategory)
//                    Log.d(
//                        "CoreRepository",
//                        "Add-ID${assetCategory.id} Location is ${assetCategory.description}, "
//                    )
//                }
//
//                val toResponse = AssetCategoryResponse(
//                    data = assetCategorys,
//                    code = 0,
//                    message = feedbackMsg
//                )
//                Log.d("CoreRepository", "getLocation count: ${toResponse.message} ")
//                return Result.success(toResponse)
//
//
//            } else {
//                return Result.success(AssetCategoryResponse(emptyList(), 1, "unknown error"))
//            }
//        } catch (e: Exception) {
//            return Result.success(AssetCategoryResponse(emptyList(), 1, "unknown error"))
//        }
//    }

    // 创建资产分类
    suspend fun submitAssetCategory(
        attrName: String,
        attrCode: String,
        arrtParentId: Int
    ): Result<SubmitResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/Basic/asset_category"
            Log.d("CoreRepository", "creat AssetCategory from: $fullUrl with token: $token")
            val toSubmitAssetCategory = AssetCategoryRequest(attrCode, attrName, arrtParentId)
            Log.d(
                "CoreRepository",
                "creat AssetCategory: ${toSubmitAssetCategory.assetCategoryCode}"
            )
            val response =
                coreApiService.submitAssetCategory(fullUrl, 1, token, toSubmitAssetCategory)

            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(SubmitResponse("", -1, "Token expired"))
            } else {
                val feedbackMsg = response.body()?.message ?: "Unknown error"
                val assetCategorys: MutableList<AssetCategory> = mutableListOf()
                response.body()!!.data?.forEach { assetCategory ->
                    assetCategorys.add(assetCategory)
                    Log.d(
                        "CoreRepository",
                        "Add-ID${assetCategory.id} Location is ${assetCategory.description}, "
                    )
                }

                val toResponse = SubmitResponse(
                    data = assetCategorys,
                    code = 0,
                    message = feedbackMsg
                )
                Log.d("CoreRepository", "submitAssetCategory: ${toResponse.message} ")


                return Result.success(toResponse)
            }
        } catch (e: Exception) {
            return Result.success(SubmitResponse("", 1, "unknown error"))
        }
    }


    // 资产等级
    suspend fun submitAssetRegistration(
        assetRegistrationRequest: AssetRegistrationRequest
    ): Result<SubmitResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/AssetBusiness/asset"
            Log.d("CoreRepository-RA", "Registration Asset from: $fullUrl with token: $token")
            Log.d(
                "CoreRepository", "TO Reg Asset: " +
                        assetRegistrationRequest.assetName + " " +
                        assetRegistrationRequest.assetCode + " " +
                        assetRegistrationRequest.rfidCodeEpc + " " +
                        assetRegistrationRequest.barcode
            )
            val response =
                coreApiService.submitRegAsset(fullUrl, 1, token, assetRegistrationRequest)
            Log.d("CoreRepository", "Response code: ${response.body()?.code}")

            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(SubmitResponse("", -1, "Token expired"))
            } else {
                if (response.isSuccessful) {
                    val toResponse = SubmitResponse(
                        data = response.body()?.data.toString(),
                        code = response.body()?.code ?: 1,
                        message = response.body()?.message ?: "Unknown error"
                    )

                    Log.d("CoreRepository", "submitAssetRegistration: ${toResponse.code} ")

                    return Result.success(toResponse)
                } else {
                    return Result.success(SubmitResponse("", 1, "unknown error"))
                }
            }
        } catch (e: Exception) {
            return Result.success(SubmitResponse("", 1, "unknown error"))
        }
    }

    // 主从资产解绑
    suspend fun submitAssetUnbindingMS(
        assetUnbindingMSRequest: AssetUnbindingMSRequest
    ): Result<SubmitResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/assetbusiness/asset/unbindmaster"
            Log.d("CoreRepository-RA", "Registration Asset from: $fullUrl with token: $token")
            val response =
                coreApiService.submitUnbindingMS(fullUrl, 1, token, assetUnbindingMSRequest)
            Log.d("CoreRepository", "Response code: ${response.body()?.code}")

            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(SubmitResponse("", -1, "Token expired"))
            } else {
                if (response.isSuccessful) {
                    val toResponse = SubmitResponse(
                        data = response.body()?.data.toString(),
                        code = response.body()?.code ?: 1,
                        message = response.body()?.message ?: "Unknown error"
                    )

                    Log.d("CoreRepository", "submitAssetRegistration: ${toResponse.code} ")

                    return Result.success(toResponse)
                } else {
                    return Result.success(SubmitResponse("", 1, "unknown error"))
                }
            }
        } catch (e: Exception) {
            return Result.success(SubmitResponse("", 1, "unknown error"))
        }
    }

    // 单个/批量资产变更
    suspend fun submitAssetChange(
        assetChangeRequest: AssetChangeRequest
    ): Result<SubmitResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/assetbusiness/asset/change"
            Log.d("CoreRepository-RA", "Registration Asset from: $fullUrl with token: $token")
            val response =
                coreApiService.submitAssetChange(fullUrl, 1, token, assetChangeRequest)
            Log.d("CoreRepository", "Response code: ${response.body()?.code}")

            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(SubmitResponse("", -1, "Token expired"))
            } else {
                if (response.isSuccessful) {
                    val toResponse = SubmitResponse(
                        data = response.body()?.data.toString(),
                        code = response.body()?.code ?: 1,
                        message = response.body()?.message ?: "Unknown error"
                    )

                    Log.d("CoreRepository", "submitAssetRegistration: ${toResponse.code} ")

                    return Result.success(toResponse)
                } else {
                    return Result.success(SubmitResponse("", 1, "unknown error"))
                }
            }
        } catch (e: Exception) {
            return Result.success(SubmitResponse("", 1, "unknown error"))
        }
    }

    // 成组资产变更
    suspend fun submitAssetChangeGroup(
        assetChangeRequest: AssetChangeRequest
    ): Result<SubmitResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/assetbusiness/asset/change/group"
            Log.d("CoreRepository-RA", "Registration Asset from: $fullUrl with token: $token")
            val response =
                coreApiService.submitAssetChangeGroup(fullUrl, 1, token, assetChangeRequest)
            Log.d("CoreRepository", "Response code: ${response.body()?.code}")

            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(SubmitResponse("", -1, "Token expired"))
            } else {
                if (response.isSuccessful) {
                    val toResponse = SubmitResponse(
                        data = response.body()?.data.toString(),
                        code = response.body()?.code ?: 1,
                        message = response.body()?.message ?: "Unknown error"
                    )

                    Log.d("CoreRepository", "submitAssetRegistration: ${toResponse.code} ")

                    return Result.success(toResponse)
                } else {
                    return Result.success(SubmitResponse("", 1, "unknown error"))
                }
            }
        } catch (e: Exception) {
            return Result.success(SubmitResponse("", 1, "unknown error"))
        }
    }


    // 资产调拨
    suspend fun submitAssetAllocation(
        assetAllocationRequest: AssetAllocationRequest
    ): Result<SubmitResponse> {
        val token: String = "bearer " + appParameterRepository.getToken()
        val url: String = appParameterRepository.getBaseUrl()
        val port: Int = appParameterRepository.getBasePort()
        return try {
            val fullUrl = "${url}:${port}/api/assetbusiness/asset/transfer"
            Log.d("CoreRepository-RA", "Registration Asset from: $fullUrl with token: $token")

            val response =
                coreApiService.submitAssetAllocation(fullUrl, 1, token, assetAllocationRequest)
            Log.d("CoreRepository", "Response code: ${response.body()?.code}")

            if (response.code() == 401) {
                Log.d("CoreRepository", "Token expired")
                return Result.success(SubmitResponse("", -1, "Token expired"))
            } else {
                if (response.isSuccessful) {
                    val toResponse = SubmitResponse(
                        data = response.body()?.data.toString(),
                        code = response.body()?.code ?: 1,
                        message = response.body()?.message ?: "Unknown error"
                    )

                    Log.d("CoreRepository", "submitAssetRegistration: ${toResponse.code} ")

                    return Result.success(toResponse)
                } else {
                    return Result.success(SubmitResponse("", 1, "unknown error"))
                }
            }
        } catch (e: Exception) {
            return Result.success(SubmitResponse("", 1, "unknown error"))
        }
    }
}


