package com.hmh.hamyeonham.challenge.appadd

data class AppAddState(
    val selectedApps: List<String> = emptyList(),
    val goalHour: Long = 0,
    val goalMin: Long = 0,
) {
    val goalTime = goalHour + goalMin
}