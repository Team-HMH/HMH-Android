package com.hmh.hamyeonham.challenge.model

data class ChallengeWithUsage(
    val challengeDate: String,
    val apps: List<Usage>
) {
    data class Usage(
        val packageName: String,
        val usageTime: Long
    )
}
