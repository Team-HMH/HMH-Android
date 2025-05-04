package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.core.database.dao.UserAuthDao
import com.hmh.hamyeonham.core.database.dao.UserProfileDao
import com.hmh.hamyeonham.core.database.entity.UserAuth
import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.mapper.toDbEntity
import com.hmh.hamyeonham.login.mapper.toDomain
import com.hmh.hamyeonham.login.model.LoginInfo
import com.hmh.hamyeonham.login.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAuthRepository @Inject constructor(
    private val userAuthDao: UserAuthDao,
    private val userProfileDao: UserProfileDao,
    private val socialAuthDataStores: Map<AuthProvider, @JvmSuppressWildcards SocialAuthDataStore>
) : AuthRepository {

    override suspend fun getCurrentAuth(): Result<LoginInfo?> = runCatching {
        val userAuth = userAuthDao.getUserAuth() ?: return@runCatching null
        if (!userAuth.isLoggedIn) return@runCatching null

        val provider = AuthProvider.valueOf(userAuth.providerType)
        LoginInfo(
            userId = userAuth.userId,
            provider = provider,
            isLoggedIn = true,
            lastLoginTimestamp = userAuth.lastLoginTimestamp
        )
    }

    override suspend fun login(provider: AuthProvider): Result<LoginInfo> = runCatching {
        val dataStore = socialAuthDataStores[provider]
            ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: $provider")

        // SDK를 사용한 소셜 로그인 처리
        val userInfo = dataStore.login()
            ?: throw IllegalStateException("소셜 로그인에 실패했습니다")

        // 로그인 정보 DB에 저장
        val userAuth = UserAuth(
            userId = userInfo.id,
            providerType = provider.name,
            isLoggedIn = true,
            lastLoginTimestamp = System.currentTimeMillis()
        )
        userAuthDao.insertOrUpdateUserAuth(userAuth)

        // 사용자 프로필 정보 DB에 저장
        val userProfile = userInfo.profile?.toDbEntity()
        if (userProfile != null) {
            userProfileDao.insertOrUpdateUserProfile(userProfile)
        }

        // 로그인 정보 반환
        LoginInfo(
            userId = userAuth.userId,
            provider = provider,
            isLoggedIn = true,
            lastLoginTimestamp = userAuth.lastLoginTimestamp
        )
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        val userAuth = userAuthDao.getUserAuth() ?: return@runCatching Unit // 이미 로그아웃 상태

        // 현재 로그인된 제공자 확인
        val provider = AuthProvider.valueOf(userAuth.providerType)
        val dataStore = socialAuthDataStores[provider]
            ?: throw IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: $provider")

        // SDK를 사용한 소셜 로그아웃 처리
        dataStore.logout()

        // DB에서 로그인 상태 업데이트
        userAuthDao.updateLoginStatus(false)
    }

    override suspend fun isLoggedIn(): Result<Boolean> = runCatching {
        val userAuth = userAuthDao.getUserAuth() ?: return@runCatching false
        userAuth.isLoggedIn
    }

    override fun observeLoginStatus(): Flow<Boolean> {
        return userAuthDao.observeUserAuth().map { userAuth ->
            userAuth?.isLoggedIn ?: false
        }
    }

    override suspend fun getUserProfile(): Result<UserProfile?> = runCatching {
        val userAuth = userAuthDao.getUserAuth() ?: return@runCatching null
        if (!userAuth.isLoggedIn) return@runCatching null

        val dbProfile = userProfileDao.getUserProfile(userAuth.userId) ?: return@runCatching null
        dbProfile.toDomain()
    }

    override fun observeUserProfile(): Flow<UserProfile?> {
        return userAuthDao.observeUserAuth().map { userAuth ->
            if (userAuth == null || !userAuth.isLoggedIn) return@map null
            val profile = userProfileDao.getUserProfile(userAuth.userId) ?: return@map null
            profile.toDomain()
        }
    }
}