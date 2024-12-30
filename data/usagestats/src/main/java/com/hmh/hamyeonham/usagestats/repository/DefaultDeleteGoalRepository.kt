package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.common.time.getCurrentDateOfDefaultTimeZone
import com.hmh.hamyeonham.usagestats.datasource.local.DeletedAppUsageLocalDataSource
import javax.inject.Inject

class DefaultDeleteGoalRepository @Inject constructor(
    private val deletedAppUsageLocalDataSource: DeletedAppUsageLocalDataSource
) : DeleteGoalRepository {
    override suspend fun getDeletedUsage(date: String): Long =
        deletedAppUsageLocalDataSource.getDeletedUsageByDate(date)

    override suspend fun getDeletedUsageOfToday(): Long {
        val today = getCurrentDateOfDefaultTimeZone().toString()
        return getDeletedUsage(today)
    }

    override suspend fun addDeletedGoal(packageName: String, usage: Long) {
        val today = getCurrentDateOfDefaultTimeZone().toString()
        deletedAppUsageLocalDataSource.addDeletedUsage(today, packageName, usage)
    }

    override suspend fun checkAndRevertDeletedGoal(packageName: String) {
        val today = getCurrentDateOfDefaultTimeZone().toString()
        deletedAppUsageLocalDataSource.checkAndDeleteDeletedUsage(today, packageName)
    }
}