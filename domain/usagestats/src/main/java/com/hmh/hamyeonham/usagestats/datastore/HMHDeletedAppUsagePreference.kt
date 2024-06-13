package com.hmh.hamyeonham.usagestats.datastore

interface HMHDeletedAppUsagePreference {
    var totalUsage: Long
    var deletedPackageName: List<String>
    fun clear()
}
