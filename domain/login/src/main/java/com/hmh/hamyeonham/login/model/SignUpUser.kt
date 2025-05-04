package com.hmh.hamyeonham.login.model

data class SignUpUser(
    val userId: Long,
    val accessToken: String,
    val refreshToken: String,
)
