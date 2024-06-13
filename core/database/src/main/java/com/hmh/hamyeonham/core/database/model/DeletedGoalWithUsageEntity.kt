package com.hmh.hamyeonham.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "deleted_goal_with_usages",
    foreignKeys = [ForeignKey(
        entity = DeletedUsageEntity::class,
        parentColumns = ["date"],
        childColumns = ["date"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DeletedGoalWithUsageEntity(
    @PrimaryKey val date: String,
    val totalUsage: Long
)