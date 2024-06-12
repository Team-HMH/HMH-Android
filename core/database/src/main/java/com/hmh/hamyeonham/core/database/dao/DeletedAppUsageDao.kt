package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hmh.hamyeonham.core.database.model.DeletedAppUsageEntity

@Dao
interface DeletedAppUsageDao {
    @Query("SELECT deletedTotalUsage FROM deleted_app_usage WHERE challengeDate = :date LIMIT 1")
    suspend fun getDeletedAppUsage(date: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedAppUsage(deletedAppUsageEntity: DeletedAppUsageEntity)

    @Query("DELETE FROM deleted_app_usage")
    suspend fun deleteAll()
}
