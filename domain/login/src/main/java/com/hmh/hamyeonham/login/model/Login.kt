package com.hmh.hamyeonham.login.model

data class Login(
    val userId: Int,
    val accessToken: String,
    val refreshToken: String,
)
