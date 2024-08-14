package com.crty.ams.core.data.repository

import android.util.Log
import com.crty.ams.AppParameter
import com.crty.ams.core.data.datastore.di.AppParameterDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class AppParameterRepository @Inject constructor(
    private val appParameterDataStore: AppParameterDataStore
) {
    val appParameterFlow: Flow<AppParameter> = appParameterDataStore.dataStoreInstance.data
        .map { appParameter ->
            // If any field in AppParameter is empty, replace with defaults
            if (appParameter.baseUrl.isEmpty() || appParameter.basePort == 0) {
                appParameter.toBuilder()
                    .setBaseUrl("http://ams.huqcc.com") // Default base URL
                    .setBasePort(8080) // Default base port
                    .build()
            } else {
                appParameter
            }
        }

    suspend fun updateAppParameter(appParameter: AppParameter) {
        appParameterDataStore.dataStoreInstance.updateData { appParameter }
    }

    suspend fun setRandomAppParameter() {
        val randomValue = Random.nextInt()
        Log.d("AppParameterRepository", "setRandomAppParameter: $randomValue")
        appParameterDataStore.dataStoreInstance.updateData {
            it.toBuilder().setRandom(randomValue).build()
        }
    }
}

