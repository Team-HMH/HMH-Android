package com.hmh.hamyeonham.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.core.network.auth.datastore.DefaultHMHNetworkPreference
import com.hmh.hamyeonham.login.repository.LogoutRepository
import com.hmh.hamyeonham.login.repository.WithdrawalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UserEffect {
    data object logoutSuccess : UserEffect
    data object logoutFail : UserEffect

    data object withdrawalSuccess : UserEffect

    data object withdrawalFail : UserEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logoutRepository: LogoutRepository,
    private val withdrawalRepository: WithdrawalRepository,
    private val hmhPreferenceAccessToken: DefaultHMHNetworkPreference,
) : ViewModel() {

    private val _userEffect = MutableSharedFlow<UserEffect>()
    val userEffect = _userEffect.asSharedFlow()

    fun handleLogout() {
        viewModelScope.launch {
            logoutRepository.logout().onSuccess {
                _userEffect.emit(UserEffect.logoutSuccess)
                hmhPreferenceAccessToken.clear()
                Log.d("hmhPreferenceAccessToken", hmhPreferenceAccessToken.accessToken)
            }.onFailure {
                _userEffect.emit(UserEffect.logoutFail)
            }
        }
    }

    fun handleWithdrawal() {
        viewModelScope.launch {
            withdrawalRepository.withdrawal().onSuccess {
                _userEffect.emit(UserEffect.withdrawalSuccess)
                hmhPreferenceAccessToken.clear()
                Log.d("hmhPreferenceAccessToken", hmhPreferenceAccessToken.accessToken)
            }.onFailure {
                _userEffect.emit(UserEffect.withdrawalFail)
            }
        }
    }
}
