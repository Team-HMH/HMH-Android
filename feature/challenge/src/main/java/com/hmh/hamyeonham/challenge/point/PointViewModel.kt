package com.hmh.hamyeonham.challenge.point

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

    private val _pointInfoList = MutableStateFlow(emptyList<PointInfo.ChallengePointStatus>())
    val pointInfoList = _pointInfoList.asStateFlow()

    init {
        getPointInfoList()
    }

    fun earnChallengePoint(challengeDate: String) {
        viewModelScope.launch {
            pointRepository.earnPoint(challengeDate).onSuccess {
                getPointInfoList()
            }
        }
    }

    private fun getPointInfoList() {
        viewModelScope.launch {
            pointRepository.getPointInfoList().onSuccess {
                _pointInfoList.value = it.challengePointStatuses
                _currentUserPoint.value = it.currentUserPoint
            }
        }
    }
}

