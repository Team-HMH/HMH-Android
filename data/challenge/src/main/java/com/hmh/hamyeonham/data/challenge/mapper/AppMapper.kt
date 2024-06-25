package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.core.network.challenge.model.AppsRequest

fun AppsRequest.toApps(): Apps {
    return Apps(
        apps = apps?.map {
            Apps.App(
                appCode = it.appCode ?: "",
                goalTime = it.goalTime ?: 0
            )
        } ?: emptyList()
    )
}

fun Apps.toAppsRequest(): AppsRequest {
    return AppsRequest(
        apps = apps.map {
            AppsRequest.App(
                appCode = it.appCode,
                goalTime = it.goalTime
            )
        }
    )
}
