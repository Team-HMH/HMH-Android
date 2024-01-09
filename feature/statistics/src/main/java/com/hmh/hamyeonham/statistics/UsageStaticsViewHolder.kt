package com.hmh.hamyeonham.statistics

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.time.convertTimeToString
import com.hmh.hamyeonham.common.view.initAndStartProgressBarAnimation
import com.hmh.hamyeonham.feature.statistics.databinding.ItemUsagestaticBinding
import com.hmh.hamyeonham.usagestats.model.UsageStatAndGoal

class UsageStaticsViewHolder(
    private val binding: ItemUsagestaticBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(usageStatAndGoal: UsageStatAndGoal) {
        binding.run {
            tvItemusagestatAppname.text =
                context.getAppNameFromPackageName(usageStatAndGoal.packageName)
            ivItemusagestatAppicon.setImageDrawable(
                context.getAppIconFromPackageName(
                    usageStatAndGoal.packageName,
                ),
            )
            tvItemusagestatTimeLeft.text = convertTimeToString(usageStatAndGoal.timeLeft)
            tvItemusagestatGoalTime.text = convertTimeToString(usageStatAndGoal.goalTime)
            pbItemUsagestat.progress = usageStatAndGoal.usedPercentage
        }
        initAndStartProgressBarAnimation(binding.pbItemUsagestat, usageStatAndGoal.usedPercentage)
    }
}
