package com.hmh.hamyeonham.common.ui.appadd.appselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.hmh.hamyeonham.common.databinding.ItemAppBinding
import com.hmh.hamyeonham.common.ui.appadd.appselection.AppSelectionViewHolder
import com.hmh.hamyeonham.common.view.ItemDiffCallback

class AppSelectionAdapter(
    private val onAppCheckboxClicked: (String) -> Unit,
    private val onAppCheckboxUnClicked: (String) -> Unit
) :
    ListAdapter<String, AppSelectionViewHolder>(
        ItemDiffCallback(onItemsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }, onContentsTheSame = { oldItem, newItem ->
            oldItem == newItem
        })
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppSelectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppBinding.inflate(inflater, parent, false)
        return AppSelectionViewHolder(
            binding,
            onAppCheckboxClicked = onAppCheckboxClicked,
            onAppCheckboxUnClicked = onAppCheckboxUnClicked
        )
    }

    override fun onBindViewHolder(holder: AppSelectionViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }
}