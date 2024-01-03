package com.hmh.hamyeonham.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.statistics.databinding.ItemUsagestaticBinding
import com.hmh.hamyeonham.feature.statistics.databinding.ItemUsagestaticTotalBinding
import com.hmh.hamyeonham.usagestats.model.UsageStatAndGoal

class UsageStaticsAdapter : ListAdapter<UsageStatAndGoal, RecyclerView.ViewHolder>(
    ItemDiffCallback<UsageStatAndGoal>(
        onItemsTheSame = { old, new -> old.packageName == new.packageName },
        onContentsTheSame = { old, new -> old == new },
    ),
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            TOTAL_ITEM_TYPE -> {
                val binding = ItemUsagestaticTotalBinding.inflate(inflater, parent, false)
                return UsageStaticsTotalViewHolder(binding, parent.context)
            }

            else -> {
                val binding = ItemUsagestaticBinding.inflate(inflater, parent, false)
                return UsageStaticsViewHolder(binding, parent.context)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (position) {
            0 -> {
                val holder = holder as UsageStaticsTotalViewHolder
                holder.onBind(currentList[position])
            }

            else -> {
                val holder = holder as UsageStaticsViewHolder
                holder.onBind(currentList[position])
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
