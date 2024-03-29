package com.hmh.hamyeonham.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.database.model.UsageGoalsEntity
import com.hmh.hamyeonham.core.database.model.UsageTotalGoalEntity

@Database(
    entities = [UsageGoalsEntity::class, UsageTotalGoalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HMHRoomDatabase : RoomDatabase() {
    abstract fun usageGoalsDao(): UsageGoalsDao
    abstract fun usageTotalGoalDao(): UsageTotalGoalDao
}
