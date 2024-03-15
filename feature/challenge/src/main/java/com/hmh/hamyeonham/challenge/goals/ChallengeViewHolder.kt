package com.hmh.hamyeonham.challenge.goals

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hmh.hamyeonham.challenge.ModifierState
import com.hmh.hamyeonham.challenge.UsageGoalAndModifierState
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.feature.challenge.databinding.ItemGoalAddBinding
import com.hmh.hamyeonham.feature.challenge.databinding.ItemUsageGoalBinding


sealed class ChallengeViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    enum class ChallengeViewHolderType {
        USAGE_GOALS,
        GOAL_ADD
    }

    class UsageGoalsViewHolder(
        private val binding: ItemUsageGoalBinding,
        private val onAppItemClicked: (UsageGoalAndModifierState) -> Unit
    ) : ChallengeViewHolder(binding) {
        private var item: UsageGoalAndModifierState? = null

        init {
            binding.root.setOnClickListener {
                item?.let(onAppItemClicked)
            }
        }

        fun bind(item: UsageGoalAndModifierState) {
            this.item = item
            binding.run {
                val context = root.context
                val packageName = item.usageGoal.packageName
                val appName = context.getAppNameFromPackageName(packageName)
                ivAppIcon.setImageDrawable(context.getAppIconFromPackageName(packageName))
                tvAppName.text = appName
                tvAppNameDelete.text = appName
                tvAppUsageGoal.text = item.usageGoal.formattedGoalTime
                tvAppUsageGoalDelete.text = item.usageGoal.formattedGoalTime
                setVisibility(item.modifierState)
            }
        }

        fun setVisibility(modifierState: ModifierState) {
            val editMode = if (modifierState == ModifierState.EDIT) View.INVISIBLE else View.VISIBLE
            val doneMode = if (modifierState != ModifierState.EDIT) View.INVISIBLE else View.VISIBLE
            binding.tvAppName.visibility = doneMode
            binding.tvAppUsageGoal.visibility = doneMode

            binding.tvAppNameDelete.visibility = editMode
            binding.tvAppUsageGoalDelete.visibility = editMode
            binding.ivTrash.visibility = editMode
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

