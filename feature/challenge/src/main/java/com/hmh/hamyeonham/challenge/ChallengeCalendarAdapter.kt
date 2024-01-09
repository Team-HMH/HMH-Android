package com.hmh.hamyeonham.challenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemChallengeStatusBinding

class ChallengeCalendarAdapter :
    ListAdapter<Boolean, ChallengeCalendarAdapter.ChallengeStatusViewHolder>(
        ItemDiffCallback<Boolean>(
            onItemsTheSame = { oldItem, newItem ->
                oldItem == newItem
            },
            onContentsTheSame = { oldItem, newItem ->
                oldItem == newItem
            }
        )
    ) {
    class ChallengeStatusViewHolder(
        private val binding: ItemChallengeStatusBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(isSuccess: Boolean?, position: Int) {
            binding.apply {
                val date = (position + 1).toString()
                tvDate.text = date
                ivChallengeStatus.setImageResource(getDrawableResource(isSuccess))
            }
        }

        private fun getDrawableResource(isSuccess: Boolean?): Int {
            return when (isSuccess) {
                true -> R.drawable.ic_challenge_success
                false -> R.drawable.ic_challenge_fail
                else -> R.drawable.challenge_shape_background_radius8
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeStatusViewHolder {
        return ChallengeStatusViewHolder(
            ItemChallengeStatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChallengeStatusViewHolder, position: Int) {
        holder.bind(currentList.getOrNull(position), position)
    }
}
