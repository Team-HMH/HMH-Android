package com.hmh.hamyeonham.challenge.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.challenge.model.Status
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemChallengeStatusBinding

class ChallengeCalendarAdapter :
    ListAdapter<Status, ChallengeCalendarAdapter.ChallengeStatusViewHolder>(
        ItemDiffCallback<Status>(
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
        fun bind(isSuccess: Status, position: Int) {
            binding.apply {
                val date = (position + 1).toString()
                tvDate.text = date
                ivChallengeStatus.setImageResource(getDrawableResource(isSuccess))
            }
        }

        private fun getDrawableResource(isSuccess: Status?): Int {
            return when (isSuccess) {
                Status.UNEARNED -> R.drawable.ic_challenge_success_38
                Status.EARNED -> R.drawable.ic_challenge_success_38
                Status.FAILURE -> R.drawable.ic_challenge_fail_38
                else -> R.drawable.ic_challenge_none_38
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
