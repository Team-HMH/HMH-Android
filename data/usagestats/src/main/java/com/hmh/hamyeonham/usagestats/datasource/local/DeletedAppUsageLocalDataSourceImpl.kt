package com.hmh.hamyeonham.usagestats.datasource.local

import com.hmh.hamyeonham.core.database.dao.DeletedGoalsDao
import javax.inject.Inject

class DeletedAppUsageLocalDataSourceImpl @Inject constructor(
    private val deletedGoalsDao: DeletedGoalsDao
) : DeletedAppUsageLocalDataSource {
    override suspend fun getDeletedUsageByDate(date: String): Long {
        return deletedGoalsDao.getTotalUsageByDate(date)
    }

    override suspend fun addDeletedUsage(
        date: String,
        packageName: String,
        appUsage: Long
    ) {
        deletedGoalsDao.addDeletedGoal(date, packageName, appUsage)
    }

    override suspend fun deleteDeletedUsage(date: String, packageName: String) {
        deletedGoalsDao.deleteUsageEntity(date, packageName)
    }


}
