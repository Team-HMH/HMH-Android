package com.hmh.hamyeonham.core.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "deleted_usage",
    indices = [Index(value = ["date"], unique = true)]
) // 고유 인덱스 추가
data class DeletedUsageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val packageName: String,
    val usage: Long,
    val date: String // 날짜 추가
)