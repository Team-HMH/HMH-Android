package com.hmh.hamyeonham.data.device.repository

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.context.isSystemPackage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultDeviceRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceRepository {

    override suspend fun getInstalledApps(): List<AppInfo> =
        withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfoList = context.packageManager.queryIntentActivities(intent, 0)
            resolveInfoList
                .map { it.toAppInfo() }
                .filter { !context.isSystemPackage(it.packageName) }
                .distinct()
        }


    private fun ResolveInfo.toAppInfo(): AppInfo {
        val packageName = activityInfo.packageName
        return AppInfo(
            packageName = packageName,
            appName = context.getAppNameFromPackageName(packageName)
        )
    }
}