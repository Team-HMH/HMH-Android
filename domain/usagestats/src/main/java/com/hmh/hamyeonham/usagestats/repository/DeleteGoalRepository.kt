package com.hmh.hamyeonham.usagestats.repository

interface DeleteGoalRepository {
    suspend fun getDeletedUsage(date: String): Long
    suspend fun getDeletedUsageOfToday(): Long

    suspend fun addDeletedGoal(packageName: String, usage: Long)

    suspend fun checkAndRevertDeletedGoal(packageName: String)
}