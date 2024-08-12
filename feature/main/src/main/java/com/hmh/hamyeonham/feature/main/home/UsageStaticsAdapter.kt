package com.hmh.hamyeonham.feature.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticBinding
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticTotalBinding
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal

data class UsageStaticsModel(
    val userName: String,
    val challengeSuccess: Boolean,
    val usageStatusAndGoal: UsageStatusAndGoal,
    val previousUsedPercentage: Int = 0
)

class UsageStaticsAdapter : ListAdapter<UsageStaticsModel, RecyclerView.ViewHolder>(
    ItemDiffCallback<UsageStaticsModel>(
        onItemsTheSame = { old, new -> old.usageStatusAndGoal == new.usageStatusAndGoal },
        onContentsTheSame = { old, new -> old == new },
    ),
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TOTAL_ITEM_TYPE -> {
                val binding = ItemUsagestaticTotalBinding.inflate(inflater, parent, false)
                UsageStaticsTotalViewHolder(binding, parent.context)
            }

            else -> {
                val binding = ItemUsagestaticBinding.inflate(inflater, parent, false)
                UsageStaticsViewHolder(binding, parent.context)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (position) {
            0 -> {
                val newHolder = holder as UsageStaticsTotalViewHolder
                newHolder.onBind(currentList.getOrNull(position) ?: return)
            }

            else -> {
                val newHolder = holder as UsageStaticsViewHolder
                newHolder.onBind(currentList.getOrNull(position) ?: return)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TOTAL_ITEM_TYPE else APP_ITEM_TYPE
    }

    companion object {
        const val TOTAL_ITEM_TYPE = 0
        const val APP_ITEM_TYPE = 1
    }
}
