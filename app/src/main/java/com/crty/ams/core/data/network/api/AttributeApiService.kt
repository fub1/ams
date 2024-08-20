package com.crty.ams.core.data.network.api


import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.data.model.CreateAttributeRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.POST
import retrofit2.http.Path


interface AttributeApiService {
    @GET("{entityType}")
    suspend fun getAllAttributes(@Path("entityType") entityType: String): List<AttributeEntity<*>>

    @POST("{entityType}")
    suspend fun createAttribute(
        @Path("entityType") entityType: String,
        @Body request: CreateAttributeRequest
    ): AttributeEntity<*>
}