package com.hmh.hamyeonham.login.datasource.kakao

import android.content.Context
import com.hmh.hamyeonham.login.model.UserInfo
import com.hmh.hamyeonham.login.repository.SocialAuthDataStore
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.AuthCodeClient.Companion.DEFAULT_REQUEST_CODE
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class KakaoSocialAuthDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SocialAuthDataStore {

    override suspend fun login(): UserInfo? {
        return try {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                loginWithKakaoTalk()
            } else {
                loginWithAccount()
            }

            // 로그인 성공 후 사용자 정보 조회
            fetchUserInfo()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return AuthApiClient.instance.hasToken()
    }

    override suspend fun logout() {
        return suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    continuation.resume(Unit)
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }

    override suspend fun refreshUserInfo(): UserInfo? {
        return try {
            fetchUserInfo()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun loginWithKakaoTalk(): Unit = suspendCancellableCoroutine { continuation ->
        val codeVerifier = KakaoAuthCodeClient.codeVerifier()
        KakaoAuthCodeClient.instance.authorizeWithKakaoTalk(
            context,
            prompts = null,
            DEFAULT_REQUEST_CODE,
            nonce = null,
            channelPublicIds = null,
            serviceTerms = null,
            codeVerifier = codeVerifier
        ) { code, codeError ->
            if (codeError != null) {
                continuation.resume(Unit)
            } else {
                AuthApiClient.instance.issueAccessToken(code!!, codeVerifier) { _, tokenError ->
                    continuation.resume(Unit)
                }
            }
        }
    }

    private suspend fun loginWithAccount(): Unit = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoAccount(context) { _, error ->
            continuation.resume(Unit)
        }
    }

    private suspend fun fetchUserInfo(): UserInfo? = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.me { user, error ->
            if (error != null || user == null) {
                continuation.resume(null)
                return@me
            }

            val profile = user.kakaoAccount?.profile
            val userInfo = UserInfo(
                id = user.id ?: -1,
                profile = UserInfo.Profile(
                    userId = user.id ?: -1,
                    nickname = profile?.nickname,
                    profileImageUrl = profile?.profileImageUrl,
                    email = user.kakaoAccount?.email,
                    ageRange = user.kakaoAccount?.ageRange?.name,
                    gender = user.kakaoAccount?.gender?.name
                )
            )

            continuation.resume(userInfo)
        }
    }
}