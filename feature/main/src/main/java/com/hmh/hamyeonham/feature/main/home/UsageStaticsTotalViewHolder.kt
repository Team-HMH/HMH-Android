package com.hmh.hamyeonham.feature.main.home

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.colorSecondStrAndBindText
import com.hmh.hamyeonham.common.time.convertTimeToString
import com.hmh.hamyeonham.common.view.initAndStartProgressBarAnimation
import com.hmh.hamyeonham.feature.main.R
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticTotalBinding
import com.hmh.hamyeonham.usagestats.model.BlackHole
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal

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
        context.colorSecondStrAndBindText(
            convertTimeToString(usageStatusAndGoal.timeLeftInMin),
            getString(
                context,
                R.string.all_left,
            ),
            binding.tvTotalTimeLeft,
            com.hmh.hamyeonham.core.designsystem.R.color.gray1,
        )
        initAndStartProgressBarAnimation(
            binding.pbTotalUsage,
            usageStatusAndGoal.usedPercentage,
        )
        bindBlackhole(usageStaticsModel, usageStaticsModel.challengeSuccess)
        setBlackholeLoop()
    }

    fun bindBlackhole(usageStaticsModel: UsageStaticsModel, challengeSucess: Boolean) {
        if (challengeSucess) {
            bindBlackHoleVideo(usageStaticsModel.usageStatusAndGoal.getBlackHoleUri())
            bindBlackholeDescriptionWithModel(
                usageStaticsModel.usageStatusAndGoal,
                usageStaticsModel.userName,
            )
        } else {
            binding.vvBlackhole.visibility = View.GONE
            bindBlackholeDescriptionWithString(
                getString(context, R.string.blackhole_fail),
            )
        }
    }

    private fun setBlackholeLoop() {
        binding.vvBlackhole.setOnCompletionListener {
            binding.vvBlackhole.start()
        }
    }

    private fun bindBlackHoleVideo(uri: String) {
        val uri = Uri.parse(uri)
        binding.vvBlackhole.run {
            setVideoURI(uri)
            requestFocus()
            setOnPreparedListener {
                it.start()
            }
        }
    }

    private fun bindBlackholeDescriptionWithModel(
        userStatusAndGoal: UsageStatusAndGoal,
        userName: String,
    ) {
        binding.tvBlackholeDescription.text = when (userStatusAndGoal.blackHole) {
            BlackHole.FIRST -> userName
            else -> ""
        } + userStatusAndGoal.getBlackHoleDescription()
        binding.tvBlackholeDescription.bringToFront()
    }

    private fun bindBlackholeDescriptionWithString(
        str: String,
    ) {
        binding.tvBlackholeDescription.text = str
    }

    fun pause() {
        binding.vvBlackhole.pause()
    }
}
