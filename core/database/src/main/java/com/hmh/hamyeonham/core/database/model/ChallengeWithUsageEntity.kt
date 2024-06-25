package com.hmh.hamyeonham.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ChallengeWithUsageEntity(
    @Embedded val challenge: DailyChallengeEntity,
    @Relation(
        parentColumn = "challengeDate",
        entityColumn = "challengeDate"
    )
    val apps: List<UsageEntity>
)