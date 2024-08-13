package com.hmh.hamyeonham.common.view

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BaseInterpolator
import android.widget.ProgressBar

fun ProgressBar.setProgressWithAnimation(progressTo: Int) {
    progress = progressTo
    initAndStartAnimation(
        view = this,
        this.progress,
        progressTo,
        AccelerateInterpolator()
    )
}

inline fun <reified T : BaseInterpolator> initAndStartAnimation(
    view: View,
    from: Int,
    to: Int,
    newInterpolator: T,
) {
    ObjectAnimator.ofInt(view, view::class.simpleName.toString(), from, to).apply {
        interpolator = newInterpolator
    }.start()
}
