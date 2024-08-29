package com.crty.ams.core.data.repository

// CoreRepository.kt


import android.util.Log
import com.crty.ams.core.data.datastore.di.AppParameterDataStore
import com.crty.ams.core.data.network.api.CoreApiService
import com.crty.ams.core.data.network.model.LoginRequest
import com.crty.ams.core.data.network.model.LoginResponse
import com.crty.ams.core.data.network.model.SystemStampResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CoreRepository @Inject constructor(
    private val coreApiService: CoreApiService,
    private val appParameterDataStore: AppParameterDataStore
) {

    suspend fun getSystemStamp(): Result<SystemStampResponse> {
        val appParameter = appParameterDataStore.dataStoreInstance.data.first()
        return try {
            val fullUrl = "${appParameter.baseUrl}:${appParameter.basePort}/api/login/Stamp"
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

    suspend fun login(): Result<LoginResponse> {
        val appParameter = appParameterDataStore.dataStoreInstance.data.first()
        return try {
            val fullUrl = "${appParameter.baseUrl}:${appParameter.basePort}/api/login"
            Log.d("CoreRepository", "Fetching system stamp from: $fullUrl")
            val response = coreApiService.login(fullUrl, 1, LoginRequest("admin", "123456", "UM5230301811"))
            Log.i("CoreRepository-Login", "Response: ${response.body()}")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API request failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




}