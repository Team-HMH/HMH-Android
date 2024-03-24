package com.hmh.hamyeonham.challenge.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.databinding.ItemChallengeStatusBinding

class ChallengeCalendarAdapter(private val context: Context) :
    ListAdapter<ChallengeStatus.Status, ChallengeStatusViewHolder>(
        ItemDiffCallback<ChallengeStatus.Status>(
            onItemsTheSame = { oldItem, newItem ->
                oldItem == newItem
            },
            onContentsTheSame = { oldItem, newItem ->
                oldItem == newItem
            },
        ),
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeStatusViewHolder {
        return ChallengeStatusViewHolder(
            ItemChallengeStatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ), context
        )
    }

    override fun onBindViewHolder(holder: ChallengeStatusViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }
}
