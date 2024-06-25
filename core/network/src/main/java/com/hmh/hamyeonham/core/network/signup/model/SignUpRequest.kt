package com.hmh.hamyeonham.core.network.signup.model

import com.hmh.hamyeonham.login.model.SignRequestDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("challenge")
    val challenge: Challenge,
    @SerialName("onboarding")
    val onboarding: Onboarding,
    @SerialName("socialPlatform")
    val socialPlatform: String,
) {
    @Serializable
    data class Onboarding(
        @SerialName("averageUseTime")
        val averageUseTime: String,
        @SerialName("problem")
        val problem: List<String>,
    )

    @Serializable
    data class Challenge(
        @SerialName("apps")
        val app: List<App>,
        @SerialName("goalTime")
        val goalTime: Long,
        @SerialName("period")
        val period: Int,
    ) {
        @Serializable
        data class App(
            @SerialName("appCode")
            val appCode: String,
            @SerialName("goalTime")
            val goalTime: Long,
        )
    }
}

fun SignRequestDomain.toSignUpRequest(): SignUpRequest {
    return SignUpRequest(
        challenge = SignUpRequest.Challenge(
            app = challenge.app.map { app ->
                SignUpRequest.Challenge.App(
                    appCode = app.appCode,
                    goalTime = app.goalTime,
                )
            },
            goalTime = challenge.goalTime.toLong(),
            period = challenge.period,
        ),
        onboarding = SignUpRequest.Onboarding(
            averageUseTime = onboarding.averageUseTime,
            problem = onboarding.problem,
        ),
        socialPlatform = "KAKAO",
    )
}
