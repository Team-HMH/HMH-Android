package com.hmh.hamyeonham.core.database.manger

import com.hmh.hamyeonham.core.database.HMHRoomDatabase
import javax.inject.Inject

class DatabaseManager @Inject constructor(
    private val database: HMHRoomDatabase
) {
    fun deleteAll() {
        database.deleteAll()
    }
}