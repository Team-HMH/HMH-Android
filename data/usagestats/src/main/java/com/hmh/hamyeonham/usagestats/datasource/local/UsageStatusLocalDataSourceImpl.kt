package com.hmh.hamyeonham.usagestats.datasource.local

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import com.hmh.hamyeonham.usagestats.model.UsageStatsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsageStatusLocalDataSourceImpl @Inject constructor(
    private val usageStatsManager: UsageStatsManager?,
) : UsageStatusLocalDataSource {

    data class AppUsageInfo(
        val packageName: String,
        var timeInForeground: Long = 0L
    )

    override suspend fun getUsageStats(startTime: Long, endTime: Long): List<UsageStatsModel> {
        return withContext(Dispatchers.IO) {
            getUsageStatistics(
                startTime = startTime,
                endTime = endTime
            ).map { UsageStatsModel(it.packageName, it.timeInForeground) }
        }
    }

    override fun getForegroundAppPackageName(): String? {
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 10000
        val usageEvents = usageStatsManager?.queryEvents(startTime, endTime)
        var lastEvent: UsageEvents.Event? = null

        usageEvents?.let {
            while (it.hasNextEvent()) {
                val event = UsageEvents.Event()
                it.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    lastEvent = event
                }
            }
        }

        return lastEvent?.packageName
    }

    private fun getUsageStatistics(startTime: Long, endTime: Long): List<AppUsageInfo> {
        if (usageStatsManager == null) {
            return emptyList()
        }

        val usageEvents = queryUsageEvents(usageStatsManager, startTime, endTime)
        val sameEvents = collectSameEvents(usageEvents)
        val usageMap = calculateUsageTime(sameEvents, endTime)

        return usageMap.values.toList()
    }

    private fun queryUsageEvents(
        usageStatsManager: UsageStatsManager,
        startTime: Long,
        endTime: Long
    ): List<UsageEvents.Event> {
        val usageEvents = mutableListOf<UsageEvents.Event>()
        val events = usageStatsManager.queryEvents(startTime, endTime)

        while (events.hasNextEvent()) {
            val currentEvent = UsageEvents.Event()
            events.getNextEvent(currentEvent)
            if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED ||
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED
            ) {
                usageEvents.add(currentEvent)
            }
        }

        return usageEvents.sortedBy { it.timeStamp }
    }

    private fun collectSameEvents(
        usageEvents: List<UsageEvents.Event>
    ): Map<String, List<UsageEvents.Event>> {
        val sameEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()

        usageEvents.forEach { event ->
            val key = event.packageName
            sameEvents.getOrPut(key) { mutableListOf() }.add(event)
        }

        return sameEvents
    }

    private fun calculateUsageTime(
        sameEvents: Map<String, List<UsageEvents.Event>>,
        endTime: Long
    ): Map<String, AppUsageInfo> {
        val usageMap = mutableMapOf<String, AppUsageInfo>()

        sameEvents.forEach { (packageName, events) ->
            val appUsageInfo = usageMap.getOrPut(packageName) { AppUsageInfo(packageName) }
            var lastResumeTime = -1L

            events.forEach { event ->
                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        lastResumeTime = event.timeStamp
                    }

                    UsageEvents.Event.ACTIVITY_PAUSED, UsageEvents.Event.ACTIVITY_STOPPED -> {
                        appUsageInfo.timeInForeground += event.timeStamp - lastResumeTime
                        lastResumeTime = event.timeStamp
                    }
                }
            }

            if (lastResumeTime != -1L) {
                appUsageInfo.timeInForeground += endTime - lastResumeTime
            }
        }

        return usageMap
    }
}
