package com.hmh.hamyeonham.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hmh.hamyeonham.core.database.dao.ChallengeDao
import com.hmh.hamyeonham.core.database.dao.DeletedGoalUsageDao
import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.database.model.DailyChallengeEntity
import com.hmh.hamyeonham.core.database.model.UsageEntity
import com.hmh.hamyeonham.core.database.model.UsageGoalsEntity
import com.hmh.hamyeonham.core.database.model.UsageTotalGoalEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        UsageGoalsEntity::class,
        UsageTotalGoalEntity::class,
        UsageEntity::class,
        DailyChallengeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HMHRoomDatabase : RoomDatabase() {
    abstract fun usageGoalsDao(): UsageGoalsDao
    abstract fun usageTotalGoalDao(): UsageTotalGoalDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun deletedGoalUsageDao(): DeletedGoalUsageDao

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteAll() {
        GlobalScope.launch {
            usageGoalsDao().deleteAll()
            usageTotalGoalDao().deleteAll()
            challengeDao().deleteAll()
            deletedGoalUsageDao().deleteAll()
        }

    }
}
