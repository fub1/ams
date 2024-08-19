package com.crty.ams.core.data.model

// 为数据选择器提供的实体类

sealed class AttributeEntity {
    abstract val id: Int
    abstract val name: String

    data class Organization(override val id: Int,override val name: String) : AttributeEntity()
    data class Address(override val id: Int, override val name: String) : AttributeEntity()
    data class Type(override val id: Int, override val name: String) : AttributeEntity()
}