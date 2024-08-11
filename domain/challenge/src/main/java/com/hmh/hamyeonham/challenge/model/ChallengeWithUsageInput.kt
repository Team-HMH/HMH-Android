package com.hmh.hamyeonham.challenge.model

data class ChallengeWithUsageInput(
    val challengePeriodIndex: Int,
    val apps: List<Usage>
) {
    data class Usage(
        val packageName: String,
        val usageTime: Long
    )
}
