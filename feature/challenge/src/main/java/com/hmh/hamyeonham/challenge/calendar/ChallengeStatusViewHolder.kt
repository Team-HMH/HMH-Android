package com.hmh.hamyeonham.challenge.calendar

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.common.view.mapBooleanToVisibility
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemChallengeStatusBinding

class ChallengeStatusViewHolder(
    private val binding: ItemChallengeStatusBinding, private val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(challengeStatus: ChallengeStatus.Status, position: Int) {
        binding.apply {
            val date = (position + 1).toString()
            tvDate.text = date
            ivChallengeStatus.setImageResource(getDrawableResource(challengeStatus))
            tvDate.setTextColor(getColor(challengeStatus))
            ivTodayMark.visibility = getVisibility(challengeStatus)
        }
    }

    private fun getVisibility(challengeStatus: ChallengeStatus.Status) =
        (challengeStatus == ChallengeStatus.Status.TODAY).mapBooleanToVisibility()

    private fun getColor(challengeStatus: ChallengeStatus.Status?): Int {
        val colorId = when (challengeStatus) {
            ChallengeStatus.Status.UNEARNED -> com.hmh.hamyeonham.core.designsystem.R.color.gray2
            ChallengeStatus.Status.EARNED -> com.hmh.hamyeonham.core.designsystem.R.color.gray2
            ChallengeStatus.Status.FAILURE -> com.hmh.hamyeonham.core.designsystem.R.color.gray2
            ChallengeStatus.Status.TODAY -> com.hmh.hamyeonham.core.designsystem.R.color.white_text
            else -> com.hmh.hamyeonham.core.designsystem.R.color.gray3
        }
        return ContextCompat.getColor(context, colorId)
    }

    private fun getDrawableResource(challengeStatus: ChallengeStatus.Status?): Int {
        return when (challengeStatus) {
            ChallengeStatus.Status.UNEARNED -> R.drawable.ic_challenge_success_42
            ChallengeStatus.Status.EARNED -> R.drawable.ic_challenge_success_42
            ChallengeStatus.Status.FAILURE -> R.drawable.ic_challenge_fail_42
            ChallengeStatus.Status.TODAY -> R.drawable.ic_challenge_today_42
            else -> R.drawable.ic_challenge_none_42
        }
    }
}