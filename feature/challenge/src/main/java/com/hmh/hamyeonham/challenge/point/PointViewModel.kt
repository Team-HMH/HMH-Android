package com.hmh.hamyeonham.challenge.point

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.domain.point.model.PointInfo
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

    private val _pointInfoList = MutableStateFlow(emptyList<PointInfo>())
    val pointInfoList = _pointInfoList.asStateFlow()

    private val _challengePeriod = MutableStateFlow(0)
    val challengePeriod = _challengePeriod.asStateFlow()

    init {
        getPointInfoList()
    }

    fun earnChallengePoint(challengeDate: String) {
        viewModelScope.launch {
            pointRepository.earnPoint(challengeDate).onSuccess {
                _currentUserPoint.value = it.totalUserPoint
                //PointInfo(point = it.totalUserPoint)
            }
        }
    }

    private fun getPointInfoList() {
        viewModelScope.launch {
            pointRepository.getPointInfoList().onSuccess {
                _pointInfoList.value = listOf(it)
                _challengePeriod.value = it.period
                _currentUserPoint.value = it.currentUserPoint
            }
        }
    }
}