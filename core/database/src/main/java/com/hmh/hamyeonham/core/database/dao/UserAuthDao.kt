package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hmh.hamyeonham.core.database.entity.UserAuth
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAuthDao {
    @Query("SELECT * FROM user_auth WHERE id = 1")
    suspend fun getUserAuth(): UserAuth?
    
    @Query("SELECT * FROM user_auth WHERE id = 1")
    fun observeUserAuth(): Flow<UserAuth?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserAuth(userAuth: UserAuth)
    
    @Query("UPDATE user_auth SET isLoggedIn = :isLoggedIn WHERE id = 1")
    suspend fun updateLoginStatus(isLoggedIn: Boolean)
    
    @Query("DELETE FROM user_auth")
    suspend fun clearUserAuth()
} 