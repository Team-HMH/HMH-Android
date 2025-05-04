package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.model.LoginInfo
import com.hmh.hamyeonham.login.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /**
     * 현재 로그인된 사용자의 인증 정보 가져오기
     * @return 현재 로그인 제공자 및 정보
     */
    suspend fun getCurrentAuth(): Result<LoginInfo?>
    
    /**
     * 사용자 로그인 처리
     * @param provider 로그인 제공자 (KAKAO 등)
     * @return 로그인 결과 (성공 또는 실패 원인)
     */
    suspend fun login(provider: AuthProvider): Result<LoginInfo>
    
    /**
     * 사용자 로그아웃 처리
     * @return 로그아웃 결과 (성공 또는 실패 원인)
     */
    suspend fun logout(): Result<Unit>
    
    /**
     * 현재 로그인 상태 확인
     * @return 로그인 상태 결과
     */
    suspend fun isLoggedIn(): Result<Boolean>
    
    /**
     * 로그인 상태를 Flow로 관찰
     * @return 로그인 상태 Flow
     */
    fun observeLoginStatus(): Flow<Boolean>
    
    /**
     * 현재 로그인된 사용자의 프로필 정보
     * @return 사용자 프로필 데이터 결과
     */
    suspend fun getUserProfile(): Result<UserProfile?>
    
    /**
     * 사용자 프로필 정보를 Flow로 관찰
     * @return 사용자 프로필 Flow
     */
    fun observeUserProfile(): Flow<UserProfile?>
}
