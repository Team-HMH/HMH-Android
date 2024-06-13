package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hmh.hamyeonham.core.database.model.DeletedGoalWithUsageEntity
import com.hmh.hamyeonham.core.database.model.DeletedUsageEntity

@Dao
interface DeletedGoalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedUsageEntity(deletedUsageEntity: DeletedUsageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedGoalsWithUsage(deletedGoalWithUsageEntity: DeletedGoalWithUsageEntity)

    @Query("SELECT * FROM deleted_goal_with_usages WHERE date = :date")
    suspend fun getDeletedGoalsWithUsageByDate(date: String): DeletedGoalWithUsageEntity?

    @Query("DELETE FROM deleted_usage")
    suspend fun deleteAllUsageEntities()

    @Query("DELETE FROM deleted_goal_with_usages")
    suspend fun deleteAllDeletedGoalsWithUsageEntities()

    @Transaction
    suspend fun deleteAll() {
        deleteAllUsageEntities()
        deleteAllDeletedGoalsWithUsageEntities()
    }

    @Transaction
    suspend fun getTotalUsageByDate(date: String): Long {
        val deletedGoalsWithUsageEntity = getDeletedGoalsWithUsageByDate(date)
        return deletedGoalsWithUsageEntity?.totalUsage ?: 0
    }

    @Transaction
    suspend fun addDeletedUsageToDate(date: String, newUsage: DeletedUsageEntity) {
        // Insert the new usage entity
        insertDeletedUsageEntity(newUsage.copy(date = date))

        // Get existing deleted goals with usage entity
        val existingDeletedGoalsWithUsage = getDeletedGoalsWithUsageByDate(date)

        if (existingDeletedGoalsWithUsage != null) {
            // Update total usage
            val newTotalUsage = existingDeletedGoalsWithUsage.totalUsage + newUsage.usage
            // Update the deleted goals with usage entity with the new total usage
            insertDeletedGoalsWithUsage(existingDeletedGoalsWithUsage.copy(totalUsage = newTotalUsage))
        } else {
            // If no existing entry, create a new one
            insertDeletedGoalsWithUsage(DeletedGoalWithUsageEntity(date, newUsage.usage))
        }
    }

    @Transaction
    suspend fun addDeletedGoal(date: String, packageName: String, usage: Long) {
        val newUsage = DeletedUsageEntity(packageName = packageName, usage = usage, date = date)
        addDeletedUsageToDate(date, newUsage)
    }

    @Transaction
    suspend fun deleteUsageEntity(date: String, packageName: String) {
        val deletedUsageEntity = getDeletedUsageByDateAndPackage(date, packageName)
        if (deletedUsageEntity != null) {
            deleteDeletedUsageEntity(deletedUsageEntity)

            // Update total usage after deletion
            val existingDeletedGoalsWithUsage = getDeletedGoalsWithUsageByDate(date)
            if (existingDeletedGoalsWithUsage != null) {
                val newTotalUsage =
                    existingDeletedGoalsWithUsage.totalUsage - deletedUsageEntity.usage
                insertDeletedGoalsWithUsage(existingDeletedGoalsWithUsage.copy(totalUsage = newTotalUsage))
            }
        }
        // else: if deletedUsageEntity is null, do nothing
    }

    @Query("SELECT * FROM deleted_usage WHERE date = :date AND packageName = :packageName")
    suspend fun getDeletedUsageByDateAndPackage(
        date: String,
        packageName: String
    ): DeletedUsageEntity?

    @Delete
    suspend fun deleteDeletedUsageEntity(deletedUsageEntity: DeletedUsageEntity)

}
