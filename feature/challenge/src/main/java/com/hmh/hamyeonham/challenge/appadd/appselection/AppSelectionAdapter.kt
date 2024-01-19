package com.hmh.hamyeonham.challenge.appadd.appselection

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.databinding.ItemAppBinding

class AppSelectionAdapter(
    private val onAppCheckboxClicked: (String) -> Unit,
    private val onAppCheckboxUnClicked: (String) -> Unit,
) :
    ListAdapter<Pair<String, Boolean>, AppSelectionAdapter.AppSelectionViewHolder>(
        ItemDiffCallback(onItemsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }, onContentsTheSame = { oldItem, newItem ->
            oldItem == newItem
        }),
    ) {
    private val checkBoxStatus = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppSelectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppBinding.inflate(inflater, parent, false)
        return AppSelectionViewHolder(
            binding,
            onAppCheckboxClicked = onAppCheckboxClicked,
            onAppCheckboxUnClicked = onAppCheckboxUnClicked,
        )
    }

    override fun onBindViewHolder(
        holder: AppSelectionViewHolder,
        position: Int,
    ) {
        holder.onBind(currentList[position])
    }

    inner class AppSelectionViewHolder(
        private val binding: ItemAppBinding,
        private val onAppCheckboxClicked: (String) -> Unit,
        private val onAppCheckboxUnClicked: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(packagePair: Pair<String, Boolean>) {
            val packageName = packagePair.first
            val isChecked = packagePair.second
            binding.run {
                val context = binding.root.context
                tvAppname.text = context.getAppNameFromPackageName(packageName)
                ivAppicon.setImageDrawable(context.getAppIconFromPackageName(packageName))
                cbApp.isChecked = isChecked || checkBoxStatus[adapterPosition]
                cbApp.isClickable = false
            }
            initAppSelectionListener(packageName)
        }

        private fun initAppSelectionListener(packageName: String) {
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
