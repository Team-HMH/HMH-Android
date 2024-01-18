package com.hmh.hamyeonham.challenge.model

data class Apps(
    val apps: List<App>
) {
    data class App(
        val appCode: String,
        val goalTime: Long
    )
}
