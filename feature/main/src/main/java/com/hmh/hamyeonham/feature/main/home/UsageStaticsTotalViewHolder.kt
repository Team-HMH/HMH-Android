package com.hmh.hamyeonham.feature.main.home

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getSecondStrColoredString
import com.hmh.hamyeonham.common.time.convertMillisecondToString
import com.hmh.hamyeonham.common.view.setProgressWithAnimation
import com.hmh.hamyeonham.core.viewmodel.HomeItem
import com.hmh.hamyeonham.feature.main.R
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticTotalBinding

class UsageStaticsTotalViewHolder(
    private val binding: ItemUsagestaticTotalBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(totalModel: HomeItem.TotalModel) {
        bindUsageStaticsInfo(totalModel)
        bindBlackHoleInfo(totalModel)
        binding.pbTotalUsage.setProgressWithAnimation(totalModel.usageAppStatusAndGoal.usedPercentage)
    }

    private fun bindUsageStaticsInfo(totalModel: HomeItem.TotalModel) {
        binding.run {
            val totalTimeLeft =
                totalModel.totalGoalTime - totalModel.totalTimeInForeground

            tvTotalTimeLeft.text =
                context.getSecondStrColoredString(
                    firstStr = convertMillisecondToString(totalTimeLeft),
                    secondStr = getString(context, R.string.all_left),
                    color = com.hmh.hamyeonham.core.designsystem.R.color.gray1,
                )
            tvTotalGoal.text =
                context.getString(
                    R.string.total_goal_time,
                    convertMillisecondToString(totalModel.totalGoalTime),
                )
            tvTotalUsage.text =
                context.getString(
                    R.string.total_used,
                    convertMillisecondToString(totalModel.totalTimeInForeground),
                )
            pbTotalUsage.progress = totalModel.usageAppStatusAndGoal.usedPercentage
        }

    }

    private fun bindBlackHoleInfo(totalModel: HomeItem.TotalModel) {
        val blackHoleInfo =
            when {
                // 권한이 허용되어 있는 경우
                totalModel.challengeSuccess -> {
                    BlackHoleInfo.createByPercentage(totalModel.usageAppStatusAndGoal.usedPercentage)
                        ?: BlackHoleInfo.LEVEL0
                }
                // 권한이 허용되지 않은 경우 default 값
                else -> {
                    BlackHoleInfo.LEVEL_NONE
                }
            }

        setBlackHoleAnimation(blackHoleInfo)
        bindBlackHoleDescription(blackHoleInfo)
    }

    private fun setBlackHoleAnimation(blackHoleInfo: BlackHoleInfo) {
        binding.lavBlackhole.setAnimation(blackHoleInfo.lottieFile)
    }

    private fun bindBlackHoleDescription(blackHoleInfo: BlackHoleInfo) {
        binding.tvBlackholeDescription.text = getString(context, blackHoleInfo.description)
    }
}

enum class BlackHoleInfo(
    val minPercentage: Int,
    val lottieFile: Int,
    val description: Int,
) {
    LEVEL_NONE(999, R.raw.lottie_blackhole0, R.string.blackhole_none),
    LEVEL0(0, R.raw.lottie_blackhole0, R.string.blackhole0),
    LEVEL1(25, R.raw.lottie_blackhole1, R.string.blackhole1),
    LEVEL2(50, R.raw.lottie_blackhole2, R.string.blackhole2),
    LEVEL3(
        75,
        R.raw.lottie_blackhole3,
        R.string.blackhole3,
    ),
    LEVEL4(100, R.raw.lottie_blackhole4, R.string.blackhole4),
    LEVEL5(-1, R.raw.lottie_blackhole5, R.string.blackhole5),
    ;

    companion object {
        private const val LEVELSIZE = 25

        fun createByPercentage(percentage: Int): BlackHoleInfo? =
            entries.find { (it.minPercentage <= percentage) && (percentage < (it.minPercentage + LEVELSIZE)) }
    }
}
