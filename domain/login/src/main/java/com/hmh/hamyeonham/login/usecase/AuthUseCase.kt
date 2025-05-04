package com.hmh.hamyeonham.login.usecase

import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.model.LoginInfo
import com.hmh.hamyeonham.login.model.UserProfile
import com.hmh.hamyeonham.login.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    /**
     * 현재 로그인된 사용자의 인증 정보 가져오기
     */
    suspend fun getCurrentAuth(): Result<LoginInfo?> {
        return authRepository.getCurrentAuth()
    }

    /**
     * 소셜 로그인 수행
     */
    suspend fun login(provider: AuthProvider): Result<LoginInfo> {
        return authRepository.login(provider)
    }

    /**
     * 로그아웃 수행
     */
    suspend fun logout(): Result<Unit> {
        return authRepository.logout()
    }

    /**
     * 회원 탈퇴 처리
     * 로그아웃 후 사용자 데이터 모두 삭제
     */
    suspend fun withdrawal(): Result<Unit> {
        // 회원 탈퇴는 현재 별도 프로세스가 없어 로그아웃과 동일하게 처리함
        // 추후 서버 API 연동 필요시 여기에 구현
        return authRepository.logout()
    }

    /**
     * 현재 로그인 상태 확인
     */
    suspend fun isLoggedIn(): Result<Boolean> {
        return authRepository.isLoggedIn()
    }

    /**
     * 사용자 프로필 정보 가져오기
     */
    suspend fun getUserProfile(): Result<UserProfile?> {
        return authRepository.getUserProfile()
    }

    /**
     * 로그인 상태를 Flow로 관찰
     */
    fun observeLoginStatus(): Flow<Boolean> {
        return authRepository.observeLoginStatus()
    }

    /**
     * 사용자 프로필 정보를 Flow로 관찰
     */
    fun observeUserProfile(): Flow<UserProfile?> {
        return authRepository.observeUserProfile()
    }
}
