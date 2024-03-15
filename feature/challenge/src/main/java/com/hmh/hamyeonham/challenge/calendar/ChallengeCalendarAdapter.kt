package com.hmh.hamyeonham.challenge.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemChallengeStatusBinding

class ChallengeCalendarAdapter :
    ListAdapter<ChallengeStatus.Status, ChallengeCalendarAdapter.ChallengeStatusViewHolder>(
        ItemDiffCallback<ChallengeStatus.Status>(
            onItemsTheSame = { oldItem, newItem ->
                oldItem == newItem
            },
            onContentsTheSame = { oldItem, newItem ->
                oldItem == newItem
            },
        ),
    ) {
    class ChallengeStatusViewHolder(
        private val binding: ItemChallengeStatusBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(isSuccess: ChallengeStatus.Status, position: Int) {
            binding.apply {
                val date = (position + 1).toString()
                tvDate.text = date
                ivChallengeStatus.setImageResource(getDrawableResource(isSuccess))
            }
        }

        private fun getDrawableResource(isSuccess: ChallengeStatus.Status?): Int {
            return when (isSuccess) {
                ChallengeStatus.Status.UNEARNED -> R.drawable.ic_challenge_success_42
                ChallengeStatus.Status.EARNED -> R.drawable.ic_challenge_success_42
                ChallengeStatus.Status.FAILURE -> R.drawable.ic_challenge_fail_42
                ChallengeStatus.Status.TODAY -> R.drawable.ic_challenge_today_42
                else -> R.drawable.ic_challenge_none_42
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeStatusViewHolder {
        return ChallengeStatusViewHolder(
            ItemChallengeStatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ChallengeStatusViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }
}
