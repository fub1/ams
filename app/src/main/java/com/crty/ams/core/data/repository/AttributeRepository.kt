package com.crty.ams.core.data.repository

import com.crty.ams.core.data.model.AttributeEntity
import com.crty.ams.core.data.model.CreateAttributeRequest
import com.crty.ams.core.data.network.api.AttributeApiService
import javax.inject.Inject
import kotlin.reflect.KClass

// 定义实体类型枚举
enum class EntityType {
    ORGANIZATION,
    ADDRESS,
    TYPE
}

// 定义自定义异常
class InvalidEntityTypeException(message: String) : Exception(message)

class AttributeRepository @Inject constructor(
    private val apiService: AttributeApiService
) {

    suspend fun <T : AttributeEntity<*>> getAllAttributes(
        entityType: String,
        targetType: KClass<out T>
    ): List<T> {
        val response = apiService.getAllAttributes(entityType.lowercase())
        @Suppress("UNCHECKED_CAST")
        return response.filter { targetType.isInstance(it) } as List<T>
    }

    suspend fun createAttribute(
        entityType: EntityType,
        name: String,
        parentId: Int
    ): AttributeEntity<*> {
        val request = CreateAttributeRequest(parentId, name)
        val response = apiService.createAttribute(entityType.name.lowercase(), request)
        return when (entityType) {
            EntityType.ORGANIZATION -> (response as? AttributeEntity.Organization)
                ?: throw InvalidEntityTypeException("Invalid entity type: $entityType")
            EntityType.ADDRESS -> (response as? AttributeEntity.Address)
                ?: throw InvalidEntityTypeException("Invalid entity type: $entityType")
            EntityType.TYPE -> (response as? AttributeEntity.Type)
                ?: throw InvalidEntityTypeException("Invalid entity type: $entityType")
        }
    }
}