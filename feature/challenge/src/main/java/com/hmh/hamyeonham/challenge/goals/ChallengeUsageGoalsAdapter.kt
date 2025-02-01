package com.hmh.hamyeonham.challenge.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.hmh.hamyeonham.challenge.ChallengeUsageGoal
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.databinding.ItemUsageGoalBinding

class ChallengeUsageGoalsAdapter(
    private val onAppItemClicked: (ChallengeUsageGoal) -> Unit
) : ListAdapter<ChallengeUsageGoal, ChallengeViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem ->
            oldItem.usageStatusAndGoal.packageName == newItem.usageStatusAndGoal.packageName
        },
        onContentsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }
    )
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengeViewHolder {
        return ChallengeViewHolder.UsageGoalsViewHolder(
            ItemUsageGoalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onAppItemClicked = onAppItemClicked
        )
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        (holder as? ChallengeViewHolder.UsageGoalsViewHolder)?.bind(getItem(position))
    }
}
