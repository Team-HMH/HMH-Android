package com.hmh.hamyeonham.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 사용자 프로필 정보를 저장하는 엔티티
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val userId: Long,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val email: String? = null,
    val ageRange: String? = null,
    val gender: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
) 