package com.hmh.hamyeonham.core.network.challenge.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppsRequest(
    @SerialName("apps")
    val apps: List<App>? = null
) {
    @Serializable
    data class App(
        @SerialName("appCode")
        val appCode: String? = null,
        @SerialName("goalTime")
        val goalTime: Long? = null
    )
}

