package com.hmh.hamyeonham.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.core.database.manger.DatabaseManager
import com.hmh.hamyeonham.core.network.auth.datastore.network.DefaultHMHNetworkPreference
import com.hmh.hamyeonham.login.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UserEffect {
    data object LogoutSuccess : UserEffect
    data object LogoutFail : UserEffect

    data object WithdrawalSuccess : UserEffect

    data object WithdrawalFail : UserEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val hmhPreference: DefaultHMHNetworkPreference,
    private val databaseManager: DatabaseManager
) : ViewModel() {

    private val _userEffect = MutableSharedFlow<UserEffect>()
    val userEffect = _userEffect.asSharedFlow()

    fun handleLogout() {
        viewModelScope.launch {
            authRepository.logout(hmhPreference.accessToken).onSuccess {
                deleteAllDatabase()
                clearPreference()
                _userEffect.emit(UserEffect.LogoutSuccess)
            }.onFailure {
                _userEffect.emit(UserEffect.LogoutFail)
            }
        }
    }

    fun handleWithdrawal() {
        viewModelScope.launch {
            authRepository.withdrawal(hmhPreference.accessToken).onSuccess {
                deleteAllDatabase()
                clearPreference()
                _userEffect.emit(UserEffect.WithdrawalSuccess)
                Log.d("hmhPreferenceAccessToken", hmhPreference.accessToken)
            }.onFailure {
                _userEffect.emit(UserEffect.WithdrawalFail)
            }
        }
    }

    private fun clearPreference() {
        hmhPreference.clear()
    }

    private fun deleteAllDatabase() {
        viewModelScope.launch {
            databaseManager.deleteAll()
        }
    }
}
