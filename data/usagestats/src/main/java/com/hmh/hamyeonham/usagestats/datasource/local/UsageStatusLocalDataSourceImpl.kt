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

    override suspend fun getUsageStats(startTime: Long, endTime: Long): List<UsageStatsModel> {
        return withContext(Dispatchers.IO) {
            getUsageStatistics(
                startTime = startTime,
                endTime = endTime
            ).map { UsageStatsModel(it.packageName, it.totalTimeInForeground) }
        }
    }

    private fun getUsageStatistics(startTime: Long, endTime: Long): List<UsageStatsModel> {
        if (usageStatsManager == null) {
            return emptyList()
        }

        val usageEvents = queryUsageEvents(usageStatsManager, startTime, endTime)
        val sameEvents = collectSameEvents(usageEvents)
        val usageMap = calculateForegroundTime(sameEvents)

        return usageMap.map { (packageName, totalTimeInForeground) ->
            UsageStatsModel(packageName, totalTimeInForeground)
        }
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
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
            ) {
                usageEvents.add(currentEvent)
            }
        }

        return usageEvents.sortedBy { it.timeStamp }
    }

    private fun collectSameEvents(usageEvents: List<UsageEvents.Event>): Map<String, List<UsageEvents.Event>> {
        val sameEvents = mutableMapOf<String, MutableList<UsageEvents.Event>>()

        usageEvents.forEach { event ->
            val key = event.packageName
            sameEvents.getOrPut(key) { mutableListOf() }.add(event)
        }

        return sameEvents
    }

    private fun calculateForegroundTime(
        sameEvents: Map<String, List<UsageEvents.Event>>
    ): Map<String, Long> {
        val usageMap = mutableMapOf<String, Long>()
        sameEvents.forEach { (key, events) ->
            var totalForegroundTime = 0L
            var lastResumeTime = -1L

            events.forEach { event ->
                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        lastResumeTime = event.timeStamp
                    }

                    UsageEvents.Event.ACTIVITY_PAUSED -> {
                        if (lastResumeTime != -1L) {
                            totalForegroundTime += event.timeStamp - lastResumeTime
                            lastResumeTime = -1L
                        }
                    }
                }
            }

            usageMap[key] = totalForegroundTime
        }

        return usageMap
    }
}
