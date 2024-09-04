package com.crty.ams.core.data.model

open class UserAttributeEntity(
    open val id: Int, // 属性ID
    open val name: String, // 父属性ID
    open val departmentId: Int, // 属性名称
    open val departmentName: String, // 属性名称
)