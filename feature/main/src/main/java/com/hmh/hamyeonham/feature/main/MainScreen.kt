package com.hmh.hamyeonham.feature.main

enum class MainScreen {
    CHALLENGE,
    HOME,
    MY_PAGE;

    companion object {
        fun fromPosition(position: Int): MainScreen = entries[position]
    }
}