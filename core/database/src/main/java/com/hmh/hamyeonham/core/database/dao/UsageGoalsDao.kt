package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hmh.hamyeonham.core.database.model.UsageGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageGoalsDao {

    @Query("SELECT * FROM usage_goals")
    fun getUsageGoal(): Flow<List<UsageGoalEntity>>

    @Query("SELECT * FROM usage_goals WHERE packageName = :packageName")
    suspend fun getUsageGoal(packageName: String): UsageGoalEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageGoal(entity: UsageGoalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageGoalList(entities: List<UsageGoalEntity>): List<Long>

    @Update
    suspend fun updateUsageGoalsList(entity: UsageGoalEntity): Int

    @Update
    suspend fun updateUsageGoalsList(entities: List<UsageGoalEntity>): Int

    @Query("DELETE FROM usage_goals WHERE packageName = :packageName")
    suspend fun deleteByPackageName(packageName: String): Int

    @Query("DELETE FROM usage_goals")
    suspend fun deleteAll()

}
