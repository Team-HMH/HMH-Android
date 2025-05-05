package com.hmh.hamyeonham.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.login.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UserEffect {
    data object LogoutSuccess : UserEffect
    data class LogoutFail(val message: String) : UserEffect
    data object WithdrawalSuccess : UserEffect
    data class WithdrawalFail(val message: String) : UserEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private val _effect = MutableSharedFlow<UserEffect>()
    val effect = _effect.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout()
                .onSuccess {
                    _effect.emit(UserEffect.LogoutSuccess)
                }
                .onFailure { exception ->
                    _effect.emit(UserEffect.LogoutFail(exception.message ?: "로그아웃에 실패했습니다"))
                }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            authUseCase.withdrawal()
                .onSuccess {
                    _effect.emit(UserEffect.WithdrawalSuccess)
                }
                .onFailure { exception ->
                    _effect.emit(UserEffect.WithdrawalFail(exception.message ?: "회원탈퇴에 실패했습니다"))
                }
        }
    }
}
