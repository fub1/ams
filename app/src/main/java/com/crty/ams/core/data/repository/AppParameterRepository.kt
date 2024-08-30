package com.crty.ams.core.data.repository

import android.util.Log
import com.crty.ams.AppParameter
import com.crty.ams.core.data.datastore.di.AppParameterDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random
import java.util.regex.Pattern

class AppParameterRepository @Inject constructor(
    private val appParameterDataStore: AppParameterDataStore
) {
    val appParameterFlow: Flow<AppParameter> = appParameterDataStore.dataStoreInstance.data
        .map { appParameter -> appParameter
        }

    suspend fun getBaseUrl(): String {
        val appParameter = appParameterDataStore.dataStoreInstance.data.firstOrNull()
        return appParameter?.baseUrl ?: ""
    }

    suspend fun getToken(): String {
        val appParameter = appParameterDataStore.dataStoreInstance.data.firstOrNull()
        return appParameter?.token ?: ""
    }


    suspend fun getBasePort(): Int {
        val appParameter = appParameterDataStore.dataStoreInstance.data.firstOrNull()
        return appParameter?.basePort ?: 80
    }




    suspend fun updateAppParameter(appParameter: AppParameter) {
        appParameterDataStore.dataStoreInstance.updateData { appParameter }
    }

    suspend fun updateAppParameterToken(token: String) {
        val appParameter = appParameterDataStore.dataStoreInstance.data.firstOrNull()
        appParameter?.let {
            appParameterDataStore.dataStoreInstance.updateData { current ->
                current.toBuilder()
                    .setToken(token)
                    .build()
            }
        }
    }

    suspend fun setRandomAppParameter() {
        // 此方法每次app启动时，写入一个随机数到AppParameter中
        // 确保首次运行初始化.pb文件
        val randomValue = Random.nextInt()

        val appParameter = appParameterDataStore.dataStoreInstance.data.firstOrNull()
        // 判断为pb为空的条件
        Log.d("AppParameterRepository", "need init settings:${appParameter?.baseUrl.isNullOrBlank()}")

        if (appParameter?.baseUrl.isNullOrBlank()) {
            Log.d("AppParameterRepository", "Init AppParameter")
            appParameterDataStore.dataStoreInstance.updateData { it ->
                it.toBuilder()
                    .setRandom(randomValue)
                    .setBaseUrl("http://localhost")
                    .setBasePort(8000)
                    .build()
            }

            val updatedAppParameter = appParameterDataStore.dataStoreInstance.data.firstOrNull()
            updatedAppParameter?.let {
                Log.d("AppParameterRepository", "BaseUrl is: ${it.baseUrl}")
                Log.d("AppParameterRepository", "BasePort is: ${it.basePort}")
                Log.d("AppParameterRepository", "set App Random String ${it.random}")
            } ?: run {
                Log.d("AppParameterRepository", "Failed to update AppParameter")
            }
        }
    }

}

