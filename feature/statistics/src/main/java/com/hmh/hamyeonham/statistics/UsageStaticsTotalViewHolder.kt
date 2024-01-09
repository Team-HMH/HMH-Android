package com.hmh.hamyeonham.statistics

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.time.convertTimeToString
import com.hmh.hamyeonham.common.view.initAndStartProgressBarAnimation
import com.hmh.hamyeonham.feature.statistics.databinding.ItemUsagestaticTotalBinding
import com.hmh.hamyeonham.usagestats.model.UsageStatAndGoal

class UsageStaticsTotalViewHolder(
    private val binding: ItemUsagestaticTotalBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(usageStatAndGoal: UsageStatAndGoal) {
        binding.run {
            tvItemusagestaticTotalHour.text = convertTimeToString(usageStatAndGoal.goalTime)
            pbItemusagestaticTotal.progress = usageStatAndGoal.usedPercentage
            tvItemusagestaticTotalTimeLeft.text = convertTimeToString(usageStatAndGoal.timeLeft)
        }
        initAndStartProgressBarAnimation(
            binding.pbItemusagestaticTotal,
            usageStatAndGoal.usedPercentage,
        )
    }

    companion object {
        private const val LEFT = "남음"
    }
}
