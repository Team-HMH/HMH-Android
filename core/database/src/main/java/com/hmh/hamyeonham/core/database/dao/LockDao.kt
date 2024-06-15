package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hmh.hamyeonham.core.database.model.LockWithDateEntity

@Dao
interface LockDao {
    @Insert
    suspend fun insertLockWithDateEntity(lockWithDateEntity: LockWithDateEntity): Long

    @Query("SELECT * FROM lock_with_date WHERE date = :date")
    suspend fun getLockWithDate(date: String): LockWithDateEntity?

    @Query("DELETE FROM lock_with_date")
    suspend fun deleteAll()
}