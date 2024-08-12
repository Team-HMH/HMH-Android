package com.hmh.hamyeonham.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_goals")
data class UsageGoalEntity(
    @PrimaryKey val packageName: String,
    val goalTime: Long,
)
