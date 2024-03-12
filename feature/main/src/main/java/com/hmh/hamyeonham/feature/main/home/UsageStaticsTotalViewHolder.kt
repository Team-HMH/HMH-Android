package com.hmh.hamyeonham.feature.main.home

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.colorSecondStrAndBindText
import com.hmh.hamyeonham.common.time.convertTimeToString
import com.hmh.hamyeonham.common.view.initAndStartProgressBarAnimation
import com.hmh.hamyeonham.feature.main.R
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticTotalBinding

class UsageStaticsTotalViewHolder(
    private val binding: ItemUsagestaticTotalBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(usageStaticsModel: UsageStaticsModel) {
        val usageStatusAndGoal = usageStaticsModel.usageStatusAndGoal
        binding.run {
            tvTotalGoal.text = convertTimeToString(usageStatusAndGoal.goalTimeInMin)
            pbTotalUsage.progress = usageStatusAndGoal.usedPercentage
        }
        bindText(usageStaticsModel)


        initAndStartProgressBarAnimation(
            binding.pbTotalUsage,
            usageStatusAndGoal.usedPercentage,
        )
        bindBlackhole(usageStaticsModel)
    }

    private fun bindText(usageStaticsModel: UsageStaticsModel) {
        context.colorSecondStrAndBindText(
            convertTimeToString(usageStaticsModel.usageStatusAndGoal.timeLeftInMin),
            getString(
                context,
                R.string.all_left,
            ),
            binding.tvTotalTimeLeft,
            com.hmh.hamyeonham.core.designsystem.R.color.gray1,
        )
        binding.tvTotalGoal.text = context.getString(
            R.string.total_goal_time,
            convertTimeToString(usageStaticsModel.usageStatusAndGoal.goalTimeInMin)
        )
        binding.tvTotalUsage.text = context.getString(
            R.string.total_used,
            convertTimeToString(usageStaticsModel.usageStatusAndGoal.totalTimeInForegroundInMin)
        )
    }

    fun bindBlackhole(usageStaticsModel: UsageStaticsModel) {
        bindBlackHoleAnimation(usageStaticsModel)
        bindBlackHoleDiscription(usageStaticsModel)
    }

    private fun bindBlackHoleAnimation(usageStaticsModel: UsageStaticsModel) {
        val lottieFile = when (usageStaticsModel.usageStatusAndGoal.blackHoleLevel) {
            0 -> R.raw.lottie_blackhole0
            1 -> R.raw.lottie_blackhole1
            2 -> R.raw.lottie_blackhole2
            3 -> R.raw.lottie_blackhole3
            4 -> R.raw.lottie_blackhole4
            5 -> R.raw.lottie_blackhole5
            else -> 0
        }
        binding.lavBlackhole.setAnimation(lottieFile)
    }

    private fun bindBlackHoleDiscription(usageStaticsModel: UsageStaticsModel) {
        val blackHoleDiscription: Int = when (usageStaticsModel.usageStatusAndGoal.blackHoleLevel) {
            0 -> R.string.blackhole0
            1 -> R.string.blackhole1
            2 -> R.string.blackhole2
            3 -> R.string.blackhole3
            4 -> R.string.blackhole4
            5 -> R.string.blackhole5
            else -> 0
        }
        binding.tvBlackholeDescription.text = getString(context, blackHoleDiscription)
    }
}
