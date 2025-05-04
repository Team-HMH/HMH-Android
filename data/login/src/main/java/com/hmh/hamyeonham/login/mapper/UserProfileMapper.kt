package com.hmh.hamyeonham.login.mapper

import com.hmh.hamyeonham.core.database.entity.UserProfile as DbUserProfile
import com.hmh.hamyeonham.login.model.UserInfo
import com.hmh.hamyeonham.login.model.UserProfile

/**
 * 데이터베이스 엔티티 UserProfile을 도메인 모델 UserProfile로 변환
 */
fun DbUserProfile.toDomain(): UserProfile {
    return UserProfile(
        userId = this.userId,
        nickname = this.nickname,
        profileImageUrl = this.profileImageUrl,
        email = this.email,
        ageRange = this.ageRange,
        gender = this.gender
    )
}

/**
 * 도메인 모델 UserProfile을 데이터베이스 엔티티 UserProfile로 변환
 */
fun UserProfile.toDbEntity(): DbUserProfile {
    return DbUserProfile(
        userId = this.userId,
        nickname = this.nickname,
        profileImageUrl = this.profileImageUrl,
        email = this.email,
        ageRange = this.ageRange,
        gender = this.gender
    )
}

/**
 * 소셜 로그인 후 받은 UserInfo.Profile을 데이터베이스 엔티티 UserProfile로 변환
 */
fun UserInfo.Profile.toDbEntity(): DbUserProfile {
    return DbUserProfile(
        userId = this.userId,
        nickname = this.nickname,
        profileImageUrl = this.profileImageUrl,
        email = this.email,
        ageRange = this.ageRange,
        gender = this.gender
    )
} 