package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hmh.hamyeonham.core.database.model.UsageTotalGoalEntity

@Dao
interface UsageTotalGoalDao {
    @Query("SELECT * FROM usage_total_goal LIMIT 1")
    suspend fun getUsageTotalGoal(): UsageTotalGoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageTotalGoal(usageTotalGoalEntity: UsageTotalGoalEntity)

    @Query("DELETE FROM usage_total_goal")
    suspend fun deleteAll()
}
