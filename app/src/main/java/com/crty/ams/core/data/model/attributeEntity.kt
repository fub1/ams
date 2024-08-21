package com.crty.ams.core.data.model

// 为选择器提供的实体类

data class AttributeEntity(
    val id: Int, // 属性ID
    val parentId: Int, // 父属性ID
    val name: String, // 属性名称
)