package com.hmh.hamyeonham.core.date

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hmh.hamyeonham.core.worker.ChallengeDateSaveWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DateChangedReceiver @Inject constructor() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent == null) return
        if (intent.action == Intent.ACTION_DATE_CHANGED) {
            context.let { ChallengeDateSaveWorker.runAt(it) }
        }
    }
}
