package com.hmh.hamyeonham.core.network.mypage.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    @SerialName("name")
    val name: String,
    @SerialName("point")
    val point: Int
)
