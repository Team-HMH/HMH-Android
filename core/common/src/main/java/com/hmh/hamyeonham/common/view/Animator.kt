package com.hmh.hamyeonham.common.view

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BaseInterpolator
import android.widget.ProgressBar

fun initAndStartProgressBarAnimation(pb: ProgressBar, progressTo: Int) {
    initAndStartAnimation(pb, "progress", 0, progressTo, AccelerateInterpolator())
}

fun initAndStartProgressBarAnimation(pb: ProgressBar, progressFrom: Int, progressTo: Int) {
    initAndStartAnimation(pb, "progress", progressFrom, progressTo, AccelerateInterpolator())
}

inline fun <reified T : BaseInterpolator> initAndStartAnimation(
    view: View,
    propertyName: String,
    from: Int,
    to: Int,
    newInterpolator: T,
) {
    ObjectAnimator.ofInt(view, propertyName, from, to).apply {
        interpolator = newInterpolator
    }.start()
}
