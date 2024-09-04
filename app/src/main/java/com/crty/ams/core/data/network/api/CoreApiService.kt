// CoreApiService.kt
package com.crty.ams.core.data.network.api


import com.crty.ams.core.data.network.model.AssetCategoryRequest
import com.crty.ams.core.data.network.model.AssetCategoryResponse
import com.crty.ams.core.data.network.model.AssetChangeRequest
import com.crty.ams.core.data.network.model.AssetRegistrationRequest
import com.crty.ams.core.data.network.model.AssetUnbindingMSRequest
import com.crty.ams.core.data.network.model.DepartmentResponse
import com.crty.ams.core.data.network.model.LocationResponse
import com.crty.ams.core.data.network.model.LoginRequest
import com.crty.ams.core.data.network.model.LoginResponse
import com.crty.ams.core.data.network.model.SubmitResponse
import com.crty.ams.core.data.network.model.SystemStampResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

/**
 * Defines the API endpoints for fetching core system data.
 */
interface CoreApiService {

    /**
     * Fetches the system timestamp from the specified URL.
     *
     * @param fullUrl The complete URL for the system timestamp endpoint.
     * @return A [Response] object containing the [SystemStampResponse].
     */
    // API校验
    @GET
    suspend fun getSystemStamp(@Url fullUrl: String): Response<SystemStampResponse>

    // API登录
    @POST
    suspend fun login(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    // 部门查询
    @GET
    suspend fun getDepartment(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Header("Authorization") token: String
    ): Response<DepartmentResponse>


    // 地点查询
    @GET
    suspend fun getLocation(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Header("Authorization") token: String
    ): Response<LocationResponse>

    // 资产分类查询
    //AssetCategoryResponse
    @GET
    suspend fun getAssetCategory(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Header("Authorization") token: String
    ): Response<AssetCategoryResponse>

    // 资产分类创建
    @POST
    suspend fun submitAssetCategory(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Header("Authorization") token: String,
        @Body requestBody: AssetCategoryRequest
    ): Response<AssetCategoryResponse>

    // 资产等级
    @POST
    suspend fun submitRegAsset(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Header("Authorization") token: String,
        @Body requestBody: AssetRegistrationRequest
    ): Response<SubmitResponse>

    // 主从资产解绑
    @POST
    suspend fun submitUnbindingMS(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Header("Authorization") token: String,
        @Body requestBody: AssetUnbindingMSRequest
    ): Response<SubmitResponse>

    // 单个/批量资产变更
    @POST
    suspend fun submitAssetChange(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Header("Authorization") token: String,
        @Body requestBody: AssetChangeRequest
    ): Response<SubmitResponse>

    //成组资产变更
    @POST
    suspend fun submitAssetChangeGroup(
        @Url fullUrl: String,
        @Header("Language") language: Int,
        @Header("Authorization") token: String,
        @Body requestBody: AssetChangeRequest
    ): Response<SubmitResponse>




    companion object {
        /**
         * Creates an instance of the [CoreApiService].
         *
         * Note: It's recommended to use a dependency injection framework like Hilt
         * to manage the lifecycle and provide this instance.
         *
         * @return An instance of [CoreApiService].
         */
        fun create(): CoreApiService {
            val okHttpClient = OkHttpClient.Builder()
                // Set the timeout for the HTTP client
                // if the request takes longer than 10 seconds, it will be cancelled
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://localhost/")
                .client(okHttpClient)
                .build()
            return retrofit.create(CoreApiService::class.java)
        }
    }
}