package com.hmh.hamyeonham.feature.main.home

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getSecondStrColoredString
import com.hmh.hamyeonham.common.permission.PermissionDescriptionActivity
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
        if (usageStaticsModel.permissionGranted) {
            binding.run {
                tvTotalTimeLeft.text =
                    context.getSecondStrColoredString(
                        firstStr = convertTimeToString(usageStaticsModel.usageStatusAndGoal.timeLeftInMin),
                        secondStr = getString(context, R.string.all_left),
                        color = com.hmh.hamyeonham.core.designsystem.R.color.gray1,
                    )
                tvTotalGoal.text =
                    context.getString(
                        R.string.total_goal_time,
                        convertTimeToString(usageStaticsModel.usageStatusAndGoal.goalTimeInMin),
                    )
                tvTotalUsage.text =
                    context.getString(
                        R.string.total_used,
                        convertTimeToString(usageStaticsModel.usageStatusAndGoal.totalTimeInForegroundInMin),
                    )
                pbTotalUsage.progress = usageStaticsModel.usageStatusAndGoal.usedPercentage
            }
        } else {
            binding.run {
                tvTotalTimeLeft.visibility = View.GONE
                tvTotalGoal.visibility = View.GONE
                tvTotalUsage.visibility = View.GONE
                pbTotalUsage.visibility = View.GONE
                tvRequirePermission.visibility = View.VISIBLE
                btnRequirePermission.visibility = View.VISIBLE

                binding.btnRequirePermission.setOnClickListener {
                    Intent(context, PermissionDescriptionActivity::class.java).apply {
                        context.startActivity(this)
                    }
                }
            }
        }
    }

    private fun bindBlackHoleInfo(usageStaticsModel: UsageStaticsModel) {
        val blackHoleInfo =
            if (usageStaticsModel.permissionGranted) {
                if (usageStaticsModel.challengeSuccess) {
                    BlackHoleInfo.createByPercentage(
                        usageStaticsModel.usageStatusAndGoal.usedPercentage,
                    ) ?: BlackHoleInfo.LEVEL0
                } else {
                    BlackHoleInfo.LEVEL5
                }
            } else {
                // 권한 허용이 안 된 경우
                BlackHoleInfo.LEVEL_NONE
            }
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
        val LEVELSIZE = 25

        fun createByPercentage(percentage: Int): BlackHoleInfo? =
            if (percentage >100) {
                LEVEL_NONE
            } else {
                entries.find {
                    (it.minPercentage <= percentage) && (percentage < (it.minPercentage + LEVELSIZE))
                }
            }
    }
}
