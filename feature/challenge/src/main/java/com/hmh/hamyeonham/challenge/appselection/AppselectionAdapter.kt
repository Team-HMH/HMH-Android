package com.hmh.hamyeonham.challenge.appselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.databinding.ItemAppBinding

class AppselectionAdapter(
    private val onAppCheckboxClicked: (Int) -> Unit,
    private val onAppCheckboxUnClicked: (Int) -> Unit
) :
    ListAdapter<String, AppselectionViewHolder>(
        ItemDiffCallback(onItemsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }, onContentsTheSame = { oldItem, newItem ->
            oldItem == newItem
        })
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppselectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppBinding.inflate(inflater, parent, false)
        return AppselectionViewHolder(
            binding,
            onAppCheckboxClicked = onAppCheckboxClicked,
            onAppCheckboxUnClicked = onAppCheckboxUnClicked
        )
    }

    override fun onBindViewHolder(holder: AppselectionViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }
}