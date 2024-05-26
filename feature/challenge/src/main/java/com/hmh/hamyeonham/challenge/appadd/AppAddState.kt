package com.hmh.hamyeonham.challenge.appadd

import android.content.Context
import com.hmh.hamyeonham.challenge.appadd.appselection.AppSelectionModel

data class AppAddState(
    val installedApps: List<String> = emptyList(),
    val selectedApps: List<String> = emptyList(),
    val goalHour: Long = 0,
    val goalMin: Long = 0,
) {
    val goalTime = goalHour + goalMin
    fun getAppSelectionList(context: Context) = installedApps.map {
        if (it.contains(context.packageName)) {
            return@map null
        }
        AppSelectionModel(it, selectedApps.contains(it))
    }
}