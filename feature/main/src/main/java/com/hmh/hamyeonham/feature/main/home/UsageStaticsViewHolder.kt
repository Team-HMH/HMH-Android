package com.hmh.hamyeonham.feature.main.home

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.context.getSecondStrColoredString
import com.hmh.hamyeonham.common.time.convertTimeToString
import com.hmh.hamyeonham.common.view.initAndStartProgressBarAnimation
import com.hmh.hamyeonham.feature.main.R
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticBinding
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal

class UsageStaticsViewHolder(
    private val binding: ItemUsagestaticBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        usageStaticsModel: UsageStaticsModel,
    ) {
        bindAppInfo(usageStaticsModel.usageStatusAndGoal)
        bindUsageInfo(usageStaticsModel.usageStatusAndGoal)
        initAndStartProgressBarAnimation(
            binding.pbAppUsage,
            usageStaticsModel.usageStatusAndGoal.usedPercentage
        )
    }

    private fun bindAppInfo(usageStatusAndGoal: UsageStatusAndGoal) {
        binding.run {
            tvAppname.text = context.getAppNameFromPackageName(usageStatusAndGoal.packageName)
            ivAppicon.setImageDrawable(
                context.getAppIconFromPackageName(
                    usageStatusAndGoal.packageName,
                ),
            )
        }
    }

    private fun bindUsageInfo(usageStatusAndGoal: UsageStatusAndGoal) {
        binding.run {
            pbAppUsage.progress = usageStatusAndGoal.usedPercentage
            tvTotalTimeInForeground.text =
                convertTimeToString(usageStatusAndGoal.totalTimeInForegroundInMin)
            tvAppTimeLeft.text =
                context.getSecondStrColoredString(
                    firstStr = convertTimeToString(usageStatusAndGoal.timeLeftInMin),
                    secondStr = getString(context, R.string.all_left),
                    color = com.hmh.hamyeonham.core.designsystem.R.color.gray1,
                )
        }
    }
}
