package com.hmh.hamyeonham.usagestats.model

data class UsageStatus(
    val packageName: String,
    val totalTimeInForeground: Long,
) {
    val totalTimeInForegroundInMin = msToMin(totalTimeInForeground)
    private fun msToMin(time: Long) = time / 1000 / 60
}
