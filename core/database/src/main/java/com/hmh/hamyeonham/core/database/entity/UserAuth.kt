package com.hmh.hamyeonham.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 사용자 인증 정보를 저장하는 엔티티
 * 토큰은 SDK에서 관리하므로 저장하지 않음
 */
@Entity(tableName = "user_auth")
data class UserAuth(
    @PrimaryKey
    val id: Int = 1, // 항상 같은 ID 사용 (싱글 인스턴스)
    val userId: Long = -1,
    val providerType: String = "", // "KAKAO", "GOOGLE" 등 AuthProvider의 name
    val isLoggedIn: Boolean = false,
    val lastLoginTimestamp: Long = 0
) 