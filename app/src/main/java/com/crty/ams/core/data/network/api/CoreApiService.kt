// CoreApiService.kt
package com.crty.ams.core.data.network.api

import com.crty.ams.core.data.network.model.LoginRequest
import com.crty.ams.core.data.network.model.LoginResponse
import com.crty.ams.core.data.network.model.SystemStampResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

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
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://localhost/")
                .build()
            return retrofit.create(CoreApiService::class.java)
        }
    }
}