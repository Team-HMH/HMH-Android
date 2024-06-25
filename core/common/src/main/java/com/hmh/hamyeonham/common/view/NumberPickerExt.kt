package com.hmh.hamyeonham.common.view

import android.widget.NumberPicker

fun NumberPicker.setupScreentimeGoalRange(minValue: Int, maxValue: Int) {
    this.minValue = minValue
    this.maxValue = maxValue
}