package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hmh.hamyeonham.core.database.model.UsageGoalsEntity
import com.hmh.hamyeonham.core.database.model.UsageTotalGoalEntity

@Dao
interface UsageTotalGoalDao {
    @Query("SELECT * FROM usage_total_goal LIMIT 1")
    fun getUsageTotalGoal(): UsageTotalGoalEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsageTotalGoal(usageTotalGoalEntity: UsageTotalGoalEntity)

    @Query("DELETE FROM usage_total_goal")
    fun deleteAll()
}
