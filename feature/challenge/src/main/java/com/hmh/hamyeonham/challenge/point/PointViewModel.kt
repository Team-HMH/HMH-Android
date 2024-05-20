package com.hmh.hamyeonham.challenge.point

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.domain.point.repository.PointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PointViewModel @Inject constructor(private val pointRepository: PointRepository) :
    ViewModel() {

    private val _currentUserPoint = MutableStateFlow(0)
    val currentPointState = _currentUserPoint.asStateFlow()

    //    private val _clickEarnPoint = MutableStateFlow(0)
//    val clickEarnPoint = _clickEarnPoint.asStateFlow()
    init {
        getCurrentUserPoint()
    }

    private fun getCurrentUserPoint() {
        viewModelScope.launch {
            pointRepository.usePoint().onSuccess {
                _currentUserPoint.value = it.userPoint
                Log.d("PointViewModel", "getCurrentUserPoint: ${it.userPoint}")
            }
        }
    }

    fun earnChallengePoint() {
        viewModelScope.launch {
            pointRepository.earnPoint().onSuccess {
                _currentUserPoint.value = it.totalUserPoint
                PointModel(point = it.totalUserPoint)
            }
        }
    }
}