package com.hmh.hamyeonham.challenge.goals

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.feature.challenge.databinding.ItemGoalAddBinding
import com.hmh.hamyeonham.feature.challenge.databinding.ItemUsageGoalBinding


sealed class ChallengeViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    enum class ChallengeViewHolderType {
        USAGE_GOALS,
        GOAL_ADD
    }

    class UsageGoalsViewHolder(
        private val binding: ItemUsageGoalBinding,
        private val onAppItemClicked: (UsageGoal) -> Unit
    ) : ChallengeViewHolder(binding) {
        private var item: UsageGoal? = null

        init {
            binding.root.setOnClickListener {
                item?.let(onAppItemClicked)
            }
        }

        fun bind(item: UsageGoal) {
            this.item = item
            binding.run {
                val context = root.context
                val packageName = item.packageName
                val appName = context.getAppNameFromPackageName(packageName)
                ivAppIcon.setImageDrawable(context.getAppIconFromPackageName(packageName))
                tvAppName.text = appName
                tvAppUsageGoal.text = item.formattedGoalTime
            }
        }
    }

    class GoalAddViewHolder(
        binding: ItemGoalAddBinding,
        private val onAppListAddClicked: () -> Unit
    ) : ChallengeViewHolder(binding) {
        init {
            binding.root.setOnClickListener {
                onAppListAddClicked()
            }
        }
    }

}

