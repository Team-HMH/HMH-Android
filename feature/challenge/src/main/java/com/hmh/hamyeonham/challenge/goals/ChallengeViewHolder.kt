package com.hmh.hamyeonham.challenge.goals

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hmh.hamyeonham.challenge.ChallengeUsageGoal
import com.hmh.hamyeonham.challenge.ModifierState
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.time.convertTimeToString
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemUsageGoalBinding


sealed class ChallengeViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class UsageGoalsViewHolder(
        private val binding: ItemUsageGoalBinding,
        private val onAppItemClicked: (ChallengeUsageGoal) -> Unit
    ) : ChallengeViewHolder(binding) {
        private var item: ChallengeUsageGoal? = null

        init {
            binding.root.setOnClickListener {
                item?.let(onAppItemClicked)
            }
        }

        fun bind(item: ChallengeUsageGoal) {
            this.item = item
            binding.run {
                bindTextViews(item)
                setVisibility(item.modifierState)
                if (item.modifierState == ModifierState.EDIT)
                    setTrashIcon(item.isDeletable)
            }
        }

        private fun ItemUsageGoalBinding.bindTextViews(
            item: ChallengeUsageGoal
        ) {
            val context = root.context
            val packageName = item.usageStatusAndGoal.packageName
            val appName = context.getAppNameFromPackageName(packageName)
            ivAppIcon.setImageDrawable(context.getAppIconFromPackageName(packageName))
            tvAppName.text = appName
            tvAppNameDelete.text = appName
            tvAppUsageGoal.text =
                convertTimeToString(item.usageStatusAndGoal.goalTimeInMinute)
            tvAppUsageGoalDelete.text =
                convertTimeToString(item.usageStatusAndGoal.goalTimeInMinute)
        }

        private fun ItemUsageGoalBinding.setVisibility(modifierState: ModifierState) {
            val editMode = getVisibilityFromMode(modifierState, ModifierState.EDIT)
            val doneMode = getVisibilityFromMode(modifierState, ModifierState.DONE)

            tvAppName.visibility = doneMode
            tvAppUsageGoal.visibility = doneMode

            tvAppNameDelete.visibility = editMode
            tvAppUsageGoalDelete.visibility = editMode
            ivTrash.visibility = editMode
        }

        private fun getVisibilityFromMode(state: ModifierState, mode: ModifierState): Int =
            if (state == mode) View.VISIBLE else View.INVISIBLE

        private fun ItemUsageGoalBinding.setTrashIcon(isDeletable: Boolean) {
            val trashIconId =
                if (isDeletable) R.drawable.ic_calendar_trash_red_28 else R.drawable.ic_calendar_trash_28
            ivTrash.setImageResource(trashIconId)
        }
    }
}

