package com.hmh.hamyeonham.challenge.appadd

import android.content.Context
import com.hmh.hamyeonham.challenge.appadd.appselection.AppSelectionModel
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName

data class AppAddState(
    val installedApps: List<AppInfo> = emptyList(),
    val selectedApps: List<String> = emptyList(),
    val goalHour: Long = 0,
    val goalMin: Long = 0,
) {
    val goalTime = goalHour + goalMin
    fun getAppSelectionList(context: Context) = installedApps.map {
        if (it.packageName.contains(context.packageName)) {
            return@map null
        }
        AppSelectionModel(it.packageName, selectedApps.contains(it.packageName))
    }.sortedBy { context.getAppNameFromPackageName(it?.packageName ?: "") }
}