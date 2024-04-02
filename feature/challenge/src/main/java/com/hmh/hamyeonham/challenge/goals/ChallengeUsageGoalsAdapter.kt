package com.hmh.hamyeonham.challenge.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.hmh.hamyeonham.challenge.UsageStatusAndGoalAndModifier
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.databinding.ItemGoalAddBinding
import com.hmh.hamyeonham.feature.challenge.databinding.ItemUsageGoalBinding

class ChallengeUsageGoalsAdapter(
    private val onAppListAddClicked: () -> Unit,
    private val onAppItemClicked: (UsageStatusAndGoalAndModifier) -> Unit
) : ListAdapter<UsageStatusAndGoalAndModifier, ChallengeViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem ->
            oldItem.usageStatusAndGoal.packageName == newItem.usageStatusAndGoal.packageName
        },
        onContentsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }
    )
) {

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.lastIndex) {
            ChallengeViewHolder.ChallengeViewHolderType.GOAL_ADD.ordinal
        } else {
            ChallengeViewHolder.ChallengeViewHolderType.USAGE_GOALS.ordinal
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengeViewHolder {
        return when (viewType) {
            ChallengeViewHolder.ChallengeViewHolderType.USAGE_GOALS.ordinal -> ChallengeViewHolder.UsageGoalsViewHolder(
                ItemUsageGoalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onAppItemClicked = onAppItemClicked
            )

            else -> ChallengeViewHolder.GoalAddViewHolder(
                ItemGoalAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onAppListAddClicked = onAppListAddClicked
            )
        }
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        (holder as? ChallengeViewHolder.UsageGoalsViewHolder)?.bind(getItem(position))
    }
}
