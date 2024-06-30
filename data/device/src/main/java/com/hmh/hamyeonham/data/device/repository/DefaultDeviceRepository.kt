package com.hmh.hamyeonham.data.device.repository

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.context.isSystemPackage
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultDeviceRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val goalsRepository: UsageGoalsRepository,
) : DeviceRepository {
    private var installedAppCache: List<AppInfo>? = null

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    override val installedApps: StateFlow<List<AppInfo>> get() = _installedApps.asStateFlow()

    override suspend fun fetchInstalledApps(): List<AppInfo> {
        if (installedApps.value.isNotEmpty() || !installedAppCache.isNullOrEmpty()) {
            return installedApps.value
        }

        return withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfoList = context.packageManager.queryIntentActivities(intent, 0)
            val installedAppInfoList = resolveInfoList
                .map { it.toAppInfo() }
                .filter { !context.isSystemPackage(it.packageName) }
                .filter { it.packageName != context.packageName }
                .distinct()
            val usageGoals = goalsRepository.getUsageGoals().firstOrNull()
            val goalPackages = usageGoals?.map { it.packageName }?.toSet() ?: emptySet()
            installedAppInfoList
                .filter { !goalPackages.contains(it.packageName) }
                .also { installedAppCache = it }
                .also { _installedApps.value = it }
        }
    }

    override suspend fun searchApp(query: String) {
        val result = installedAppCache?.filter { it.appName.contains(query) }
        if (query.isEmpty() || result.isNullOrEmpty()) {
            _installedApps.value = installedAppCache ?: emptyList()
            return
        }
        _installedApps.value = installedApps.value.filter { it.appName.contains(query) }
    }


    private fun ResolveInfo.toAppInfo(): AppInfo {
        val packageName = activityInfo.packageName
        return AppInfo(
            packageName = packageName,
            appName = context.getAppNameFromPackageName(packageName)
        )
    }
}