package com.hmh.hamyeonham.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "usages",
    foreignKeys = [
        ForeignKey(
            entity = DailyChallengeEntity::class,
            parentColumns = ["challengeDate"],
            childColumns = ["challengeDate"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsageEntity(
    @PrimaryKey
    val packageName: String,
    val usageTime: Long,
    val challengeDate: String
)