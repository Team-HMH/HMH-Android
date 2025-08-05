package com.hmh.hamyeonham.mypage.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

sealed interface UserEffect {
    data object LogoutSuccess : UserEffect
    data class LogoutFail(val message: String) : UserEffect
    data object WithdrawalSuccess : UserEffect
    data class WithdrawalFail(val message: String) : UserEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
) : ViewModel() {

    private val _effect = MutableSharedFlow<UserEffect>()
    val effect = _effect.asSharedFlow()

    fun logout() {

    }

    fun withdrawal() {

    }
}
