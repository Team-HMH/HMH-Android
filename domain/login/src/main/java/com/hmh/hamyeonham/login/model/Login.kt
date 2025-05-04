package com.hmh.hamyeonham.login.model

data class Login(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
)
