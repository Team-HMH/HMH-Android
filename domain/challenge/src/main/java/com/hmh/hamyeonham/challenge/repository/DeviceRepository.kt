package com.hmh.hamyeonham.challenge.repository

import com.hmh.hamyeonham.challenge.model.AppInfo

interface DeviceRepository {
    fun getInstalledApps(): List<AppInfo>
}
