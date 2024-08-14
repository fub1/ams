package com.crty.ams.core.data.repository

import android.util.Log
import com.crty.ams.AppParameter
import com.crty.ams.core.data.datastore.di.AppParameterDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.random.Random

class AppParameterRepository @Inject constructor(
    private val appParameterDataStore: AppParameterDataStore
) {
    val appParameterFlow: Flow<AppParameter> = appParameterDataStore.dataStoreInstance.data

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

