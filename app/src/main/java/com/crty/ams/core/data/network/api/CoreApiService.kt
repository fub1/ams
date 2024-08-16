package com.crty.ams.core.data.network.api


import com.crty.ams.core.data.network.model.SystemStampResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CoreApiService {

    @GET
    suspend fun getSystemStamp(@Url fullUrl: String): Response<SystemStampResponse>
}