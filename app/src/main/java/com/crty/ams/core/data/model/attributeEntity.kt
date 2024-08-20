package com.crty.ams.core.data.model

// 为数据选择器提供的实体类

sealed class AttributeEntity<T>(
    open val id: Int,
    open val parentId: Int,
    open val name: String
) {
    data class Organization(
        override val id: Int,
        override val parentId: Int,
        override val name: String
    ) : AttributeEntity<Any?>(id, parentId, name)

    data class Address(
        override val id: Int,
        override val parentId: Int,
        override val name: String
    ) : AttributeEntity<Any?>(id, parentId, name)

    data class Type(
        override val id: Int,
        override val parentId: Int,
        override val name: String
    ) : AttributeEntity<Any?>(id, parentId, name)
}
data class CreateAttributeRequest(
    val parentId: Int,
    val name: String
)