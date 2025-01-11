package com.hmh.hamyeonham.feature.onboarding.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.onboarding.databinding.ItemAddAppBinding

class AppSelectionAdapter(
    private val onAppCheckboxClicked: (String) -> Unit,
    private val onAppCheckboxUnClicked: (String) -> Unit,
) :
    ListAdapter<AppInfo, AppSelectionAdapter.AppSelectionViewHolder>(
        ItemDiffCallback(
            onItemsTheSame = { oldItem, newItem ->
                oldItem == newItem
            }, onContentsTheSame = { oldItem, newItem ->
                oldItem == newItem
            }),
    ) {
    private val checkBoxStatus = SparseBooleanArray()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AppSelectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddAppBinding.inflate(inflater, parent, false)
        return AppSelectionViewHolder(
            binding,
            onAppCheckboxClicked = onAppCheckboxClicked,
            onAppCheckboxUnClicked = onAppCheckboxUnClicked,
        )
    }

    override fun onBindViewHolder(holder: AppSelectionViewHolder, position: Int) {
        currentList.getOrNull(position)?.let {
            holder.onBind(it.packageName)
        }
    }

    inner class AppSelectionViewHolder(
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
                        checkBoxStatus.put(adapterPosition, true)
                    }
                }
            }
        }
    }
}
