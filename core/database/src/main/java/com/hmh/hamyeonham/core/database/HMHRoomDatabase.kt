package com.hmh.hamyeonham.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hmh.hamyeonham.core.database.dao.ChallengeDao
import com.hmh.hamyeonham.core.database.dao.DeletedGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.database.model.DailyChallengeEntity
import com.hmh.hamyeonham.core.database.model.DeletedGoalWithUsageEntity
import com.hmh.hamyeonham.core.database.model.DeletedUsageEntity
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
        DailyChallengeEntity::class,
        DeletedGoalWithUsageEntity::class,
        DeletedUsageEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class HMHRoomDatabase : RoomDatabase() {
    abstract fun usageGoalsDao(): UsageGoalsDao
    abstract fun usageTotalGoalDao(): UsageTotalGoalDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun deletedGoalsDao(): DeletedGoalsDao

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteAll() {
        GlobalScope.launch {
            usageGoalsDao().deleteAll()
            usageTotalGoalDao().deleteAll()
            challengeDao().deleteAll()
            deletedGoalsDao().deleteAll()
        }

    }

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // deleted_goal_with_usages 테이블 생성
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `deleted_goal_with_usages` (`date` TEXT NOT NULL, `totalUsage` INTEGER NOT NULL, PRIMARY KEY(`date`), FOREIGN KEY(`date`) REFERENCES `deleted_usage`(`date`) ON DELETE CASCADE)"
                )
                // deleted_usage 테이블 생성
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `deleted_usage` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `packageName` TEXT NOT NULL, `usage` INTEGER NOT NULL, `date` TEXT NOT NULL)"
                )
                // deleted_usage 테이블에 date 열에 고유 인덱스 추가
                database.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_deleted_usage_date` ON `deleted_usage` (`date`)"
                )
            }
        }

    }
}
