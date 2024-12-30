package com.hmh.hamyeonham.core.network.main.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BannerResponse(
    @SerialName("title") val title: String? = null,
    @SerialName("subTitle") val subTitle: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("linkUrl") val linkUrl: String? = null,
    @SerialName("backgroundColors") val backgroundColors: List<String>? = null
)
