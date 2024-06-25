package com.hmh.hamyeonham.login.model

data class SignRequestDomain(
    val challenge: Challenge,
    val onboarding: Onboarding,
) {
    data class Onboarding(
        val averageUseTime: String,
        val problem: List<String>,
    )

    data class Challenge(
        val app: List<App>,
        val goalTime: Long,
        val period: Int,
    ) {
        data class App(
            val appCode: String,
            val goalTime: Long,
        )
    }
}
