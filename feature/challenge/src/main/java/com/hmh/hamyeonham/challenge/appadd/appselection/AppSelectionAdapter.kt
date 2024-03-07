package com.hmh.hamyeonham.challenge.appadd.appselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.databinding.ItemAppBinding

class AppSelectionAdapter(
    private val onAppChecked: (String) -> Unit,
    private val onAppUnChecked: (String) -> Unit,
) : ListAdapter<AppSelectionModel, AppSelectionAdapter.AppSelectionViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem ->
            oldItem == newItem
        },
        onContentsTheSame = { oldItem, newItem ->
            oldItem.packageName == newItem.packageName
        }
    ),
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppSelectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppBinding.inflate(inflater, parent, false)
        return AppSelectionViewHolder(
            binding,
            onAppChecked = onAppChecked,
            onAppUnChecked = onAppUnChecked,
        )
    }

    override fun onBindViewHolder(holder: AppSelectionViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class AppSelectionViewHolder(
        private val binding: ItemAppBinding,
        private val onAppChecked: (String) -> Unit,
        private val onAppUnChecked: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(appSelectionModel: AppSelectionModel) {
            val packageName = appSelectionModel.packageName
            binding.run {
                val context = root.context
                tvAppname.text = context.getAppNameFromPackageName(packageName)
                ivAppicon.setImageDrawable(context.getAppIconFromPackageName(packageName))
            }
            updateCheckedAppListner(appSelectionModel)
        }

        private fun updateCheckedAppListner(appSelectionModel: AppSelectionModel) {
            binding.run {
                cbApp.setOnCheckedChangeListener(null)
                cbApp.isChecked = appSelectionModel.isChecked
                root.setOnClickListener {
                    cbApp.isChecked = !cbApp.isChecked
                }

                cbApp.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        onAppChecked(appSelectionModel.packageName)
                    } else {
                        onAppUnChecked(appSelectionModel.packageName)
                    }
                }
            }
        }
    }
}
