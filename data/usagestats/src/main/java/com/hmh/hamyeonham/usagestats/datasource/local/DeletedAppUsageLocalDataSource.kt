package com.hmh.hamyeonham.usagestats.datasource.local

interface DeletedAppUsageLocalDataSource {
    suspend fun getDeletedUsageByDate(date: String): Long
    suspend fun addDeletedUsage(date: String, packageName: String, appUsage: Long)

    suspend fun deleteDeletedUsage(date: String, packageName: String)
}
