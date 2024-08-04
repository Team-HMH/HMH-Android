package com.hmh.hamyeonham.challenge.calendar

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.view.mapBooleanToVisibility
import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemChallengeStatusBinding

class ChallengeStatusViewHolder(
    private val binding: ItemChallengeStatusBinding, private val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(challenge: ChallengeStatus, position: Int) {
        binding.apply {
            val date = (position + 1).toString()
            tvDate.text = date
            ivChallengeStatus.setImageResource(getDrawableResource(challenge))
            tvDate.setTextColor(getColor(challenge))
            ivTodayMark.visibility = getChallengeStatusVisibility(challenge)
        }
    }

    private fun getChallengeStatusVisibility(challenge: ChallengeStatus) =
        (challenge == ChallengeStatus.TODAY).mapBooleanToVisibility()

    private fun getColor(challenge: ChallengeStatus?): Int {
        val colorId = when (challenge) {
            ChallengeStatus.UNEARNED -> com.hmh.hamyeonham.core.designsystem.R.color.gray2
            ChallengeStatus.EARNED -> com.hmh.hamyeonham.core.designsystem.R.color.gray2
            ChallengeStatus.FAILURE -> com.hmh.hamyeonham.core.designsystem.R.color.gray2
            ChallengeStatus.TODAY -> com.hmh.hamyeonham.core.designsystem.R.color.white_text
            else -> com.hmh.hamyeonham.core.designsystem.R.color.gray3
        }
        return ContextCompat.getColor(context, colorId)
    }

    private fun getDrawableResource(challenge: ChallengeStatus?): Int {
        return when (challenge) {
            ChallengeStatus.UNEARNED -> R.drawable.ic_challenge_success_42
            ChallengeStatus.EARNED -> R.drawable.ic_challenge_success_42
            ChallengeStatus.FAILURE -> R.drawable.ic_challenge_fail_42
            ChallengeStatus.TODAY -> R.drawable.ic_challenge_today_42
            else -> R.drawable.ic_challenge_none_42
        }
    }
}