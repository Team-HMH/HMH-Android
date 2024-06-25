package com.hmh.hamyeonham.core.network.challenge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppCodeRequest(
    @SerialName("appCode")
    private val appCode: String? = null
)
