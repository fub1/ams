package com.crty.ams.core.data.datastore.di

import com.crty.ams.AppParameter
import com.crty.ams.core.data.datastore.AppParameterSerializer
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class AppParameterDataStore @Inject constructor(private val context: Context) {
    private val Context.dataStore: DataStore<AppParameter> by dataStore(
        fileName = "App_Parameter.pb",
        serializer = AppParameterSerializer
    )

    val dataStoreInstance: DataStore<AppParameter>
        get() = context.dataStore
}
