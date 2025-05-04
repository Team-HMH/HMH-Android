package com.hmh.hamyeonham.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.core.database.manger.DatabaseManager
import com.hmh.hamyeonham.core.network.auth.datastore.network.DefaultUserPreference
import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.usecase.AuthUseCase
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
    private val authUseCase: AuthUseCase,
    private val hmhPreference: DefaultUserPreference,
    private val databaseManager: DatabaseManager
) : ViewModel() {

    private val _userEffect = MutableSharedFlow<UserEffect>()
    val userEffect = _userEffect.asSharedFlow()

    fun handleLogout() {
        viewModelScope.launch {
            authUseCase.logout(AuthProvider.KAKAO).onSuccess {
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
            authUseCase.withdrawal(AuthProvider.KAKAO).onSuccess {
                deleteAllDatabase()
                clearPreference()
                _userEffect.emit(UserEffect.WithdrawalSuccess)
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
