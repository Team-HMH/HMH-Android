package com.hmh.hamyeonham.login.model

/**
 * 소셜 로그인으로부터 받은 사용자 정보
 */
data class UserInfo(
    val id: Long,
    val profile: Profile? = null
) {
    /**
     * 사용자 프로필 정보
     */
    data class Profile(
        val userId: Long,
        val nickname: String? = null,
        val profileImageUrl: String? = null,
        val email: String? = null,
        val ageRange: String? = null,
        val gender: String? = null
    )
} 