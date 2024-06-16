package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hmh.hamyeonham.core.database.model.UsageGoalsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Dao
interface UsageGoalsDao {

    @Query("SELECT * FROM usage_goals")
    fun getUsageGoal(): Flow<List<UsageGoalsEntity>>

    @Query("SELECT * FROM usage_goals WHERE packageName = :packageName")
    suspend fun getUsageGoal(packageName: String): UsageGoalsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageGoal(entity: UsageGoalsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageGoalList(entities: List<UsageGoalsEntity>): List<Long>

    @Update
    suspend fun updateUsageGoalsList(entity: UsageGoalsEntity): Int

    @Update
    suspend fun updateUsageGoalsList(entities: List<UsageGoalsEntity>): Int

    @Query("DELETE FROM usage_goals WHERE packageName = :packageName")
    suspend fun deleteByPackageName(packageName: String): Int

    @Query("DELETE FROM usage_goals")
    suspend fun deleteAll()

}
