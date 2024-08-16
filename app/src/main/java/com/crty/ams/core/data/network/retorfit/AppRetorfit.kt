// AppRetrofit.kt
package com.crty.ams.core.data.network.retorfit

import android.util.Log
import com.crty.ams.AppParameter
import com.crty.ams.core.data.datastore.di.AppParameterDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRetrofit @Inject constructor(
    private val appParameterDataStore: AppParameterDataStore
) {

    private var retrofit: Retrofit? = null

    private suspend fun getBaseUrlFromDataStore(): String {
        return try {
            val appParameter: AppParameter = appParameterDataStore.dataStoreInstance.data.first()
            val baseUrl = appParameter.baseUrl
            val port = appParameter.basePort

            if (port > 0) {
                "$baseUrl:$port"
            } else {
                baseUrl
            }
        } catch (e: Exception) {
            Log.e("AppRetrofit", "Error reading base URL from DataStore: ${e.message}", e)
            // Return a default base URL
            "http://default.url"
        }
    }

    fun getRetrofitInstance(): Retrofit {
        val baseUrl = runBlocking { getBaseUrlFromDataStore() }

        if (retrofit == null || retrofit?.baseUrl()?.toString() != baseUrl) {
            Log.d("AppRetrofit", "Creating new Retrofit instance with baseUrl: $baseUrl")

            val okHttpClient = OkHttpClient.Builder()
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}