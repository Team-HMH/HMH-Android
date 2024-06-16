package com.hmh.hamyeonham.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lock_with_date")
data class LockWithDateEntity(
    @PrimaryKey
    val date: String,
    val isUnLock: Boolean? = null
)
