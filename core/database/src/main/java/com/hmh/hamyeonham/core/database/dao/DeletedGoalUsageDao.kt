package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hmh.hamyeonham.core.database.model.DeletedGoalUsageEntity

@Dao
interface DeletedGoalUsageDao {
    @Query("SELECT deletedTotalUsage FROM deleted_goal_usage WHERE challengeDate = :date LIMIT 1")
    suspend fun getDeletedAppUsage(date: String): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedAppUsage(deletedGoalUsageEntity: DeletedGoalUsageEntity)

    @Query("DELETE FROM deleted_goal_usage")
    suspend fun deleteAll()
}
