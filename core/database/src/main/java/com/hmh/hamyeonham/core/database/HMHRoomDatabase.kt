package com.hmh.hamyeonham.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hmh.hamyeonham.core.database.dao.ChallengeDao
import com.hmh.hamyeonham.core.database.dao.DeletedGoalsDao
import com.hmh.hamyeonham.core.database.dao.LockDao
import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.database.model.DailyChallengeEntity
import com.hmh.hamyeonham.core.database.model.DeletedGoalWithUsageEntity
import com.hmh.hamyeonham.core.database.model.DeletedUsageEntity
import com.hmh.hamyeonham.core.database.model.LockWithDateEntity
import com.hmh.hamyeonham.core.database.model.UsageEntity
import com.hmh.hamyeonham.core.database.model.UsageGoalEntity
import com.hmh.hamyeonham.core.database.model.UsageTotalGoalEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        UsageGoalEntity::class,
        UsageTotalGoalEntity::class,
        UsageEntity::class,
        DailyChallengeEntity::class,
        DeletedGoalWithUsageEntity::class,
        DeletedUsageEntity::class,
        LockWithDateEntity::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class HMHRoomDatabase : RoomDatabase() {
    abstract fun usageGoalsDao(): UsageGoalsDao
    abstract fun usageTotalGoalDao(): UsageTotalGoalDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun deletedGoalsDao(): DeletedGoalsDao
    abstract fun lockDao(): LockDao

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteAll() {
        GlobalScope.launch {
            usageGoalsDao().deleteAll()
            usageTotalGoalDao().deleteAll()
            challengeDao().deleteAll()
            deletedGoalsDao().deleteAll()
            lockDao().deleteAll()
        }
    }

    companion object {
        // 버전 1에서 버전 2로 마이그레이션
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // UserAuth 테이블 생성
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `user_auth` (
                        `id` INTEGER NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `providerType` TEXT NOT NULL,
                        `isLoggedIn` INTEGER NOT NULL,
                        `lastLoginTimestamp` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """
                )

                // UserProfile 테이블 생성
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `user_profile` (
                        `userId` INTEGER NOT NULL,
                        `nickname` TEXT,
                        `profileImageUrl` TEXT,
                        `email` TEXT,
                        `ageRange` TEXT,
                        `gender` TEXT,
                        `updatedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`userId`)
                    )
                    """
                )
            }
        }
    }
}
