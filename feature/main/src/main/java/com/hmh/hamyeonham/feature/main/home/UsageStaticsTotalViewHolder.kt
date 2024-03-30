package com.hmh.hamyeonham.feature.main.home

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getSecondStrColoredString
import com.hmh.hamyeonham.common.time.convertTimeToString
import com.hmh.hamyeonham.common.view.initAndStartProgressBarAnimation
import com.hmh.hamyeonham.feature.main.R
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticTotalBinding

class UsageStaticsTotalViewHolder(
    private val binding: ItemUsagestaticTotalBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(usageStaticsModel: UsageStaticsModel) {
        bindUsageStaticsInfo(usageStaticsModel)
        bindBlackHoleInfo(usageStaticsModel)
        initAndStartProgressBarAnimation(
            binding.pbTotalUsage,
            usageStaticsModel.usageStatusAndGoal.usedPercentage,
        )
    }

    private fun bindUsageStaticsInfo(usageStaticsModel: UsageStaticsModel) {
        binding.run {
            tvTotalTimeLeft.text = context.getSecondStrColoredString(
                firstStr = convertTimeToString(usageStaticsModel.usageStatusAndGoal.timeLeftInMin),
                secondStr = getString(context, R.string.all_left),
                color = com.hmh.hamyeonham.core.designsystem.R.color.gray1,
            )
            tvTotalGoal.text = context.getString(
                R.string.total_goal_time,
                convertTimeToString(usageStaticsModel.usageStatusAndGoal.goalTimeInMin)
            )
            tvTotalUsage.text = context.getString(
                R.string.total_used,
                convertTimeToString(usageStaticsModel.usageStatusAndGoal.totalTimeInForegroundInMin)
            )
            pbTotalUsage.progress = usageStaticsModel.usageStatusAndGoal.usedPercentage
        }
    }

    private fun bindBlackHoleInfo(usageStaticsModel: UsageStaticsModel) {
        val blackHoleInfo =
            BlackHoleInfo.createByLevel(usageStaticsModel.usageStatusAndGoal.blackHoleLevel)
                ?: BlackHoleInfo.LEVEL0
        setBlackHoleAnimation(blackHoleInfo)
        bindBlackHoleDiscription(blackHoleInfo)
    }

    private fun setBlackHoleAnimation(blackHoleInfo: BlackHoleInfo) {
        binding.lavBlackhole.setAnimation(blackHoleInfo.lottieFile)
    }

    private fun bindBlackHoleDiscription(blackHoleInfo: BlackHoleInfo) {
        binding.tvBlackholeDescription.text = getString(context, blackHoleInfo.description)
    }
}

enum class BlackHoleInfo(val level: Int, val lottieFile: Int, val description: Int) {
    LEVEL0(0, R.raw.lottie_blackhole0, R.string.blackhole0),
    LEVEL1(1, R.raw.lottie_blackhole1, R.string.blackhole1),
    LEVEL2(2, R.raw.lottie_blackhole2, R.string.blackhole2), LEVEL3(
        3,
        R.raw.lottie_blackhole3,
        R.string.blackhole3
    ),
    LEVEL4(4, R.raw.lottie_blackhole4, R.string.blackhole4),
    LEVEL5(5, R.raw.lottie_blackhole5, R.string.blackhole5);

    companion object {
        fun createByLevel(level: Int): BlackHoleInfo? {
            return entries.find { it.level == level }
        }
    }
}