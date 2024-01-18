package com.hmh.hamyeonham.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_total_goal")
data class UsageTotalGoalEntity(
    @PrimaryKey val id: Int = 1,
    val goalTime: Long
)
