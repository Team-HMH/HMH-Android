package com.hmh.hamyeonham.feature.onboarding.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.feature.onboarding.databinding.ItemAddAppBinding

class OnBoardingAppSelectionViewHolder(
    private val binding: ItemAddAppBinding,
    private val onAppCheckboxClicked: (String) -> Unit,
    private val onAppCheckboxUnClicked: (String) -> Unit,
) : ViewHolder(binding.root) {
    fun onBind(packageName: String) {
        binding.run {
            val context = binding.root.context
            tvAppname.text = context.getAppNameFromPackageName(packageName)
            ivAppicon.setImageDrawable(context.getAppIconFromPackageName(packageName))
            cbApp.isClickable = false
        }
        setCheckBoxButtonListener(packageName)
    }

    private fun setCheckBoxButtonListener(packageName: String) {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                if (binding.cbApp.isChecked) {
                    binding.cbApp.isChecked = false
                    onAppCheckboxUnClicked(packageName)
                } else {
                    binding.cbApp.isChecked = true
                    onAppCheckboxClicked(packageName)
                }
            }
        }
    }
}
