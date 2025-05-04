package com.hmh.hamyeonham.login.model

import com.hmh.hamyeonham.login.di.AuthProvider

/**
 * 사용자 로그인 정보를 담는 모델 클래스
 */
data class LoginInfo(
    val userId: Long,
    val provider: AuthProvider,
    val isLoggedIn: Boolean,
    val lastLoginTimestamp: Long
) 