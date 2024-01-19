package com.hmh.hamyeonham.feature.onboarding.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.onboarding.databinding.ItemAddAppBinding

class OnBoardingAppSelectionAdapter(
    private val onAppCheckboxClicked: (String) -> Unit,
    private val onAppCheckboxUnClicked: (String) -> Unit,
) :
    ListAdapter<String, OnBoardingAppSelectionAdapter.OnBoardingAppSelectionViewHolder>(
        ItemDiffCallback(onItemsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }, onContentsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }),
    ) {
    private val checkBoxStatus = SparseBooleanArray()

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

    inner class OnBoardingAppSelectionViewHolder(
        private val binding: ItemAddAppBinding,
        private val onAppCheckboxClicked: (String) -> Unit,
        private val onAppCheckboxUnClicked: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(packageName: String) {
            binding.run {
                val context = binding.root.context
                tvAppname.text = context.getAppNameFromPackageName(packageName)
                ivAppicon.setImageDrawable(context.getAppIconFromPackageName(packageName))
                cbApp.isClickable = false
                cbApp.isChecked = checkBoxStatus[adapterPosition]
            }
            setCheckBoxButtonListener(packageName)
        }

        private fun setCheckBoxButtonListener(packageName: String) {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (binding.cbApp.isChecked) {
                        binding.cbApp.isChecked = false
                        onAppCheckboxUnClicked(packageName)
                        checkBoxStatus.put(adapterPosition, false)
                    } else {
                        binding.cbApp.isChecked = true
                        onAppCheckboxClicked(packageName)
                        checkBoxStatus.put(adapterPosition, false)
                    }
                }
            }
        }
    }
}
