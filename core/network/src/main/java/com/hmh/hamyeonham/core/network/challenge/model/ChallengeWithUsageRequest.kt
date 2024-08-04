package com.hmh.hamyeonham.core.network.challenge.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeWithUsageRequest(
    @SerialName("finishedDailyChallenges")
    val finishedDailyChallenges: List<DailyChallenges>
) {
    @Serializable
    data class DailyChallenges(
        @SerialName("challengeDate")
        val challengeDate: String,
        @SerialName("apps")
        val apps: List<AppUsage>
    ) {
        @Serializable
        data class AppUsage(
            @SerialName("appCode")
            val appCode: String,
            @SerialName("usageTime")
            val usageTime: Long
        )
    }
}
