package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.login.model.UserInfo

/**
 * 소셜 로그인 구현체에 대한 인터페이스
 * 각 소셜 로그인 구현체(카카오, 구글 등)는 이 인터페이스를 구현해야 함
 */
interface SocialAuthDataStore {
    /**
     * 소셜 로그인을 수행하고 사용자 정보를 반환
     * 토큰은 SDK에서 자체 관리하므로 반환하지 않음
     * @return 로그인 성공 시 사용자 정보, 실패 시 null
     */
    suspend fun login(): UserInfo?
    
    /**
     * 현재 로그인 상태인지 확인
     * @return 로그인 상태면 true, 아니면 false
     */
    suspend fun isLoggedIn(): Boolean
    
    /**
     * 소셜 로그아웃 처리
     */
    suspend fun logout()
    
    /**
     * 프로필 정보 갱신
     * @return 최신 사용자 정보
     */
    suspend fun refreshUserInfo(): UserInfo?
}
