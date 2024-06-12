package com.hmh.hamyeonham.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "deleted_goal_usage",
    foreignKeys = [
        ForeignKey(
            entity = DailyChallengeEntity::class,
            parentColumns = ["challengeDate"],
            childColumns = ["challengeDate"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DeletedGoalUsageEntity(
    @PrimaryKey
    val challengeDate: String,
    val deletedTotalUsage: Long,
)