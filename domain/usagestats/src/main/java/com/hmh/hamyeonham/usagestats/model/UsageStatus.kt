package com.hmh.hamyeonham.usagestats.model

data class UsageStatus(
    val packageName: String,
    val totalTimeInForeground: Long,
) {
    val totalTimeInForegroundInMin = msToMin(totalTimeInForeground)
    private fun msToMin(time: Long) = time / 1000 / 60 }

fun List<UsageStatus>.sumUsageStats() = this.sumOf { it.totalTimeInForegroundInMin } * 60 * 1000L