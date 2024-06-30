package com.hmh.hamyeonham.challenge.repository

import com.hmh.hamyeonham.challenge.model.AppInfo

interface DeviceRepository {
    suspend fun getInstalledApps(): List<AppInfo>
}
