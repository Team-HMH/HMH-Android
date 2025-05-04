package com.hmh.hamyeonham.login.datasource.kakao

import android.content.Context
import com.hmh.hamyeonham.login.model.User
import com.hmh.hamyeonham.login.repository.SocialAuthDataStore
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.AuthCodeClient.Companion.DEFAULT_REQUEST_CODE
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class KakaoSocialAuthDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SocialAuthDataStore {

    override suspend fun login(): Result<String> =
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            suspendCancellableCoroutine { cont ->
                loginWithKakaoTalk(context) { token, error ->
                    when {
                        error != null -> cont.resume(Result.failure(error))
                        token != null -> cont.resume(Result.success(token.accessToken))
                        else -> cont.resume(
                            Result.failure(
                                IllegalStateException("Empty KakaoTalk token")
                            )
                        )
                    }
                }
            }
        } else {
            loginWithAccount()
        }

    private fun loginWithKakaoTalk(
        context: Context,
        requestCode: Int = DEFAULT_REQUEST_CODE,
        nonce: String? = null,
        channelPublicIds: List<String>? = null,
        serviceTerms: List<String>? = null,
        callback: (token: OAuthToken?, error: Throwable?) -> Unit,
    ) {
        val codeVerifier = KakaoAuthCodeClient.codeVerifier()
        KakaoAuthCodeClient.instance.authorizeWithKakaoTalk(
            context,
            prompts = null,
            requestCode,
            nonce = nonce,
            channelPublicIds = channelPublicIds,
            serviceTerms = serviceTerms,
            codeVerifier = codeVerifier
        ) { code, codeError ->
            if (codeError != null) {
                callback(null, codeError)
            } else {
                AuthApiClient.instance.issueAccessToken(code!!, codeVerifier) { token, tokenError ->
                    callback(token, tokenError)
                }
            }
        }
    }


    private suspend fun loginWithAccount(): Result<String> = suspendCancellableCoroutine { cont ->
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            when {
                error != null -> cont.resume(Result.failure(error))
                token != null -> cont.resume(Result.success(token.accessToken))
                else -> cont.resume(Result.failure(IllegalStateException("Empty KakaoAccount token")))
            }
        }
    }

    override suspend fun fetchUserProfile(): Result<User> =
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.me { user, error ->
                when {
                    error != null -> cont.resume(Result.failure(error))
                    user != null -> cont.resume(Result.success(User(user.id)))
                    else -> cont.resume(Result.failure(IllegalStateException("Empty Kakao user")))
                }
            }
        }

    override suspend fun logout(): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            UserApiClient.instance.logout { error ->
                if (error != null) cont.resume(Result.failure(error))
                else cont.resume(Result.success(Unit))
            }
        }
}