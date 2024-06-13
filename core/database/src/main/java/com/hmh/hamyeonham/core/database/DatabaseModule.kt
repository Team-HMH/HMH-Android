package com.hmh.hamyeonham.core.database

import android.content.Context
import androidx.room.Room
import com.hmh.hamyeonham.core.database.HMHRoomDatabase.Companion.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesHMHDatabase(
        @ApplicationContext context: Context,
    ): HMHRoomDatabase = Room.databaseBuilder(
        context,
        HMHRoomDatabase::class.java,
        "hmh-android-database",
    ).addMigrations(MIGRATION_1_2).build()
}
