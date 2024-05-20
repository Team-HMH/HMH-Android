package com.hmh.hamyeonham.core.viewmodel

sealed interface MainEffect {
    data object SuccessUsePoint : MainEffect
    data object LackOfPoint : MainEffect
}