package com.hmh.hamyeonham.feature.onboarding.model

data class OnBoardingQuestion(
    val title: String = "",
    val description: String = "",
    val options: List<String> = emptyList()
)
