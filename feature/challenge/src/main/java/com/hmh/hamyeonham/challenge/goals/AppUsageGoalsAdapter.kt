package com.hmh.hamyeonham.challenge.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.hmh.hamyeonham.challenge.ChallengeUsageGoal
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.databinding.ItemUsageGoalBinding

class AppUsageGoalsAdapter(
    private val onAppItemClicked: (ChallengeUsageGoal) -> Unit
) : ListAdapter<ChallengeUsageGoal, AppUsageGoalsViewHolder>(
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
    ): AppUsageGoalsViewHolder {
        return AppUsageGoalsViewHolder.UsageGoalsViewHolder(
            ItemUsageGoalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onAppItemClicked = onAppItemClicked
        )
    }

    override fun onBindViewHolder(holder: AppUsageGoalsViewHolder, position: Int) {
        (holder as? AppUsageGoalsViewHolder.UsageGoalsViewHolder)?.bind(getItem(position))
    }
}
