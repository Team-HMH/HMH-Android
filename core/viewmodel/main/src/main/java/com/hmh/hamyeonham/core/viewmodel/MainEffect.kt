package com.hmh.hamyeonham.core.viewmodel

sealed interface MainEffect {
    data object NetworkError : MainEffect
}