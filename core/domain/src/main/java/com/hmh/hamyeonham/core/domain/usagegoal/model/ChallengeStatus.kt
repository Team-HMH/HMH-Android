package com.hmh.hamyeonham.core.domain.usagegoal.model

enum class ChallengeStatus {
    NONE,
    UNEARNED,
    EARNED,
    FAILURE,
    TODAY;
    companion object {
        fun fromString(value: String): ChallengeStatus {
            return when (value) {
                NONE.name -> NONE
                UNEARNED.name -> UNEARNED
                EARNED.name -> EARNED
                FAILURE.name -> FAILURE
                TODAY.name -> TODAY
                else -> NONE
            }
        }
    }
}