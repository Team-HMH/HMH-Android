package com.hmh.hamyeonham.feature.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.core.viewmodel.HomeItem
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticBinding
import com.hmh.hamyeonham.feature.main.databinding.ItemUsagestaticTotalBinding

class UsageStaticsAdapter : ListAdapter<HomeItem, RecyclerView.ViewHolder>(
    ItemDiffCallback<HomeItem>(
        onItemsTheSame = { old, new -> old.hashCode() == new.hashCode() },
        onContentsTheSame = { old, new -> old == new },
    ),
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (HomeItemViewType.fromOrdinal(viewType)) {
            HomeItemViewType.TOTAL_ITEM_TYPE -> {
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
        when (holder) {
            is UsageStaticsTotalViewHolder -> {
                val item = currentList.getOrNull(position) as? HomeItem.TotalModel ?: return
                holder.onBind(item)
            }

           is UsageStaticsViewHolder -> {
                val item = currentList.getOrNull(position) as? HomeItem.UsageStaticsModel ?: return
               holder.onBind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        val itemType = when (currentItem) {
            is HomeItem.TotalModel -> HomeItemViewType.TOTAL_ITEM_TYPE
            is HomeItem.UsageStaticsModel -> HomeItemViewType.APP_ITEM_TYPE
        }
        return HomeItemViewType.getOrdinal(itemType)
    }

    enum class HomeItemViewType {
        TOTAL_ITEM_TYPE,
        APP_ITEM_TYPE,
        ;

        companion object {
            fun fromOrdinal(ordinal: Int) =
                entries.firstOrNull { it.ordinal == ordinal } ?: APP_ITEM_TYPE

            fun getOrdinal(viewType: HomeItemViewType) = viewType.ordinal
        }
    }
}
