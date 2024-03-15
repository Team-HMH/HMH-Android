package com.hmh.hamyeonham.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class OnBoardingStoryViewModel @Inject constructor() : ViewModel() {

    private val _storyClickNum = MutableStateFlow(0)
    val storyClickNum = _storyClickNum

    fun countStoryClickNum() {
        _storyClickNum.value++
    }
}
