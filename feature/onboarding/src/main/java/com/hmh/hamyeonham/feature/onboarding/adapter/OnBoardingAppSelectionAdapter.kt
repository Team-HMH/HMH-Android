package com.hmh.hamyeonham.feature.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.onboarding.databinding.ItemAddAppBinding

class OnBoardingAppSelectionAdapter(
    private val onAppCheckboxClicked: (String) -> Unit,
    private val onAppCheckboxUnClicked: (String) -> Unit,
) :
    ListAdapter<String, OnBoardingAppSelectionViewHolder>(
        ItemDiffCallback(onItemsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }, onContentsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }),
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OnBoardingAppSelectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddAppBinding.inflate(inflater, parent, false)
        return OnBoardingAppSelectionViewHolder(
            binding,
            onAppCheckboxClicked = onAppCheckboxClicked,
            onAppCheckboxUnClicked = onAppCheckboxUnClicked,
        )
    }

    override fun onBindViewHolder(holder: OnBoardingAppSelectionViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }
}
