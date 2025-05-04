package com.hmh.hamyeonham.login.model

/**
 * 사용자 프로필 정보를 담는 모델 클래스
 */
data class UserProfile(
    val userId: Long,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val email: String? = null,
    val ageRange: String? = null,
    val gender: String? = null
) 