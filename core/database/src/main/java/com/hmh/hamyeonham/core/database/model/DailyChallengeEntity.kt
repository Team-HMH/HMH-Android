package com.hmh.hamyeonham.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey
    val challengeDate: String,
)