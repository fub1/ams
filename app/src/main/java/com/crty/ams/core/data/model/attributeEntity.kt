package com.crty.ams.core.data.model

// 为选择器提供的实体类

open class AttributeEntity(
    open val id: Int, // 属性ID
    open val parentId: Int, // 父属性ID
    val name: String, // 属性名称
)