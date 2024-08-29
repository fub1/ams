package com.crty.ams.core.data.network.model

data class LoginRequest(
    val userno: String,
    val password: String,
    val sn: String
)

data class LoginResponse(
    val data: LoginData?,
    val code: Int,
    val message: String
)

data class LoginData(
    val access_token: String,
    val user: LoginUser
)

data class LoginUser(
    val username: String
)