package com.hmh.hamyeonham.challenge.newchallenge

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NewChallengeState(
    val goalDate: Int = NOTINITIALIZED,
    val goalTime: Long = DEFAULTTIME,
) {

    fun isInitialized(): Boolean = goalDate != NOTINITIALIZED

    companion object {
        val NOTINITIALIZED: Int = 0
        val DEFAULTTIME: Long = 120000
    }
}

class NewChallengeViewModel : ViewModel() {
    private val _state = MutableStateFlow(NewChallengeState())
    val state = _state.asStateFlow()

    private fun updateState(transform: NewChallengeState.() -> NewChallengeState) {
        val currentState = state.value
        val newState = currentState.transform()
        _state.value = newState
    }

    fun selectDate(date: Int) {
        updateState {
            copy(goalDate = date)
        }
    }

    fun unSelectDate() {
        updateState {
            copy(goalDate = NewChallengeState.NOTINITIALIZED)
        }
    }

    fun setGoalHour(goalTime: Long) {
        updateState {
            copy(goalTime = goalTime)
        }
    }

}