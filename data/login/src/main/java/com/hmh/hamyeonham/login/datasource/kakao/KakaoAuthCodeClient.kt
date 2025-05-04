package com.hmh.hamyeonham.login.datasource.kakao

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import com.kakao.sdk.auth.Constants
import com.kakao.sdk.auth.IntentFactory
import com.kakao.sdk.auth.SingleResultReceiver
import com.kakao.sdk.auth.UriUtility
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ApplicationInfo
import com.kakao.sdk.common.model.ApprovalType
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.AuthErrorResponse
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.ContextInfo
import com.kakao.sdk.common.util.IntentResolveClient
import com.kakao.sdk.common.util.KakaoJson
import com.kakao.sdk.common.util.SdkLog
import java.net.HttpURLConnection
import java.security.MessageDigest
import java.util.UUID

/**
 * Kakao SDK 내부의 [AuthCodeClient] 파일
 * */
class KakaoAuthCodeClient(
    private val intentResolveClient: IntentResolveClient = IntentResolveClient.instance,
    private val applicationInfo: ApplicationInfo = KakaoSdk.applicationContextInfo,
    private val contextInfo: ContextInfo = KakaoSdk.applicationContextInfo,
    private val approvalType: ApprovalType = KakaoSdk.approvalType,
) {
    private fun isKakaoTalkLoginAvailable(context: Context): Boolean =
        intentResolveClient.resolveTalkIntent(context, IntentFactory.talkBase()) != null

    @JvmOverloads
    fun authorizeWithKakaoTalk(
        context: Context,
        prompts: List<Prompt>? = null,
        requestCode: Int = DEFAULT_REQUEST_CODE,
        nonce: String? = null,
        channelPublicIds: List<String>? = null,
        serviceTerms: List<String>? = null,
        codeVerifier: String? = null,
        kauthTxId: String? = null,
        callback: (code: String?, error: Throwable?) -> Unit,
    ) {
        if (!isKakaoTalkLoginAvailable(context)) {
            callback(null, ClientError(ClientErrorCause.NotSupported, "KakaoTalk not installed"))
        } else {
            try {
                context.startActivity(
                    IntentFactory.talk(
                        context,
                        requestCode,
                        clientId = applicationInfo.appKey,
                        redirectUri = applicationInfo.redirectUri,
                        kaHeader = contextInfo.kaHeader,
                        extras = Bundle().apply {
                            channelPublicIds?.let {
                                putString(
                                    Constants.CHANNEL_PUBLIC_ID,
                                    channelPublicIds.joinToString(",")
                                )
                            }
                            serviceTerms?.let {
                                putString(
                                    Constants.SERVICE_TERMS,
                                    serviceTerms.joinToString(",")
                                )
                            }
                            approvalType.value?.let { putString(Constants.APPROVAL_TYPE, it) }
                            codeVerifier?.let {
                                putString(Constants.CODE_CHALLENGE, codeChallenge(it.toByteArray()))
                                putString(
                                    Constants.CODE_CHALLENGE_METHOD,
                                    Constants.CODE_CHALLENGE_METHOD_VALUE
                                )
                            }
                            prompts?.let { prompts ->
                                putString(
                                    Constants.PROMPT,
                                    prompts.joinToString(",") { prompt -> prompt.value }
                                )
                            }
                            nonce?.let { putString(Constants.NONCE, nonce) }
                            kauthTxId?.let { putString(Constants.KAUTH_TX_ID, kauthTxId) }
                        },
                        resultReceiver = resultReceiver(callback)
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            } catch (startActivityError: Throwable) {
                SdkLog.e(startActivityError)
                callback(null, startActivityError)
            }
        }
    }

    @JvmOverloads
    fun authorizeWithKakaoAccount(
        context: Context,
        prompts: List<Prompt>? = null,
        scopes: List<String>? = null,
        nonce: String? = null,
        agt: String? = null,
        channelPublicIds: List<String>? = null,
        serviceTerms: List<String>? = null,
        loginHint: String? = null,
        codeVerifier: String? = null,
        accountsSkipIntro: Boolean? = null,
        accountsTalkLoginVisible: Boolean? = null,
        kauthTxId: String? = null,
        callback: (code: String?, error: Throwable?) -> Unit,
    ) {
        val uriUtility = UriUtility()
        val uri =
            uriUtility.authorize(
                clientId = applicationInfo.appKey,
                agt = agt,
                redirectUri = applicationInfo.redirectUri,
                scopes = scopes,
                kaHeader = contextInfo.kaHeader,
                channelPublicIds = channelPublicIds,
                serviceTerms = serviceTerms,
                prompts = prompts,
                nonce = nonce,
                loginHint = loginHint,
                approvalType = approvalType.value,
                codeChallenge = codeVerifier?.let { codeChallenge(it.toByteArray()) },
                codeChallengeMethod = codeVerifier?.let { Constants.CODE_CHALLENGE_METHOD_VALUE },
                accountsSkipIntro = accountsSkipIntro,
                accountsTalkLoginVisible = accountsTalkLoginVisible,
                kauthTxId = kauthTxId,
            )
        SdkLog.i(uri)
        try {
            context.startActivity(
                IntentFactory.account(
                    context,
                    uri,
                    applicationInfo.redirectUri,
                    resultReceiver(callback)
                )
            )
        } catch (startActivityError: Throwable) {
            SdkLog.e(startActivityError)
            callback(null, startActivityError)
        }
    }

    @JvmSynthetic
    internal fun resultReceiver(callback: (String?, Throwable?) -> Unit) =
        SingleResultReceiver.create(
            emitter = callback,
            identifier = "Auth Code",
            parseResponse = { uri -> uri.getQueryParameter(Constants.CODE) },
            parseError = { uri ->
                // oauth spec
                // error is nonNull, errorDescription is nullable
                val error = uri.getQueryParameter(Constants.ERROR) ?: Constants.UNKNOWN_ERROR
                val errorDescription = uri.getQueryParameter(Constants.ERROR_DESCRIPTION)
                val errorCause = runCatching {
                    KakaoJson.fromJson<AuthErrorCause>(error, AuthErrorCause::class.java)
                }.getOrDefault(AuthErrorCause.Unknown)

                AuthError(
                    HttpURLConnection.HTTP_MOVED_TEMP,
                    errorCause,
                    AuthErrorResponse(error, errorDescription)
                )
            },
            isError = { uri -> uri.getQueryParameter(Constants.CODE).isNullOrEmpty() }
        )

    companion object {
        @JvmStatic
        val instance by lazy { KakaoAuthCodeClient() }

        const val DEFAULT_REQUEST_CODE: Int = 10012

        fun codeVerifier(): String =
            Base64.encodeToString(
                MessageDigest.getInstance(Constants.CODE_VERIFIER_ALGORITHM).digest(
                    UUID.randomUUID().toString().toByteArray()
                ),
                Base64.NO_WRAP or Base64.NO_PADDING
            )

        fun codeChallenge(codeVerifier: ByteArray): String =
            Base64.encodeToString(
                MessageDigest.getInstance(Constants.CODE_CHALLENGE_ALGORITHM).digest(codeVerifier),
                Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE
            )
    }
}
