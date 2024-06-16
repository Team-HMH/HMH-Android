package com.hmh.hamyeonham.core.network.lock.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LockCheckWithDate(
    @SerialName("isLockToday")
    val isLockToday: Boolean? = null
)