package com.hmh.hamyeonham.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.core.network.auth.datastore.network.UserPreference
import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginEffect {
    data object LoginSuccess : LoginEffect

    data object LoginFail : LoginEffect

    data object RequireSignUp : LoginEffect
}

data class LoginState(
    val autoLogin: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userPreference: UserPreference,
) : ViewModel() {
    private val _kakaoLoginEvent = MutableSharedFlow<LoginEffect>()
    val kakaoLoginEvent = _kakaoLoginEvent.asSharedFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    init {
        updateLoginState()
    }

    private fun updateLoginState() {
        val currentState = loginState.value
        _loginState.value = currentState.copy(
            autoLogin = userPreference.autoLoginConfigured,
        )
    }

    fun loginWithKakaoApp() {
        viewModelScope.launch {
            authUseCase.login(AuthProvider.KAKAO)
                .onSuccess {
                    // TODO if (온보딩을 타야하는 경우) _kakaoLoginEvent.emit(LoginEffect.RequireSignUp)
                    _kakaoLoginEvent.emit(LoginEffect.LoginSuccess)
                    AmplitudeUtils.trackEventWithProperties("click_onboarding_kakao")
                }.onFailure {
                    _kakaoLoginEvent.emit(LoginEffect.LoginFail)
                }
        }
    }
}
