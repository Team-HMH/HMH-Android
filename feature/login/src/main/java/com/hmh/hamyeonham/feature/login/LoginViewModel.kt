package com.hmh.hamyeonham.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
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

    data class LoginError(val message: String) : LoginEffect
}

data class LoginState(
    val autoLogin: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            authUseCase.isLoggedIn().onSuccess { isLoggedIn ->
                _loginState.value = loginState.value.copy(
                    autoLogin = isLoggedIn,
                    isLoading = false
                )
            }
        }
    }

    fun loginWithKakao() {
        loginWith(AuthProvider.KAKAO)
    }

    fun loginWithGoogle() {
        loginWith(AuthProvider.GOOGLE)
    }

    private fun loginWith(provider: AuthProvider) {
        viewModelScope.launch {
            authUseCase.login(provider)
                .onSuccess {
                    AmplitudeUtils.trackEventWithProperties("click_onboarding_kakao")
                    _effect.emit(LoginEffect.LoginSuccess)
                }
                .onFailure { exception ->
                    _effect.emit(LoginEffect.LoginError(exception.message ?: "로그인에 실패했습니다"))
                }
        }
    }
}
