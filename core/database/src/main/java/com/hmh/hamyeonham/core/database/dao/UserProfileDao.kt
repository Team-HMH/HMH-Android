package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hmh.hamyeonham.core.database.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE userId = :userId")
    suspend fun getUserProfile(userId: Long): UserProfile?
    
    @Query("SELECT * FROM user_profile WHERE userId = :userId")
    fun observeUserProfile(userId: Long): Flow<UserProfile?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserProfile(userProfile: UserProfile)
    
    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()
} 