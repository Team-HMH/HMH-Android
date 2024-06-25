package com.hmh.hamyeonham.login.model

data class SignUpUser(
    val userId: Int,
    val accessToken: String,
    val refreshToken: String,
)
