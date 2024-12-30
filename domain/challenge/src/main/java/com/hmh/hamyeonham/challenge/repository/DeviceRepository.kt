package com.hmh.hamyeonham.challenge.repository

import com.hmh.hamyeonham.challenge.model.AppInfo
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val installedApps: StateFlow<List<AppInfo>>
    suspend fun fetchInstalledApps(): List<AppInfo>
    suspend fun searchApp(query: String)
}
