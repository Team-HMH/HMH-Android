package com.hmh.hamyeonham.challenge.point

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.domain.point.model.PointInfo
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemPointBinding

class PointAdapter(
    private val onButtonClick: (String) -> Unit,
) : ListAdapter<PointInfo, PointAdapter.PointViewHolder>(
    ItemDiffCallback(
        onItemsTheSame = { oldItem, newItem ->
            oldItem == newItem
        },
        onContentsTheSame = { oldItem, newItem ->
            oldItem == newItem
        },
    ),
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPointBinding.inflate(inflater, parent, false)
        return PointViewHolder(
            binding,
            onButtonClick,
        )
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class PointViewHolder(
        private val binding: ItemPointBinding,
        private val onButtonClick: (String) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun onBind(pointModel: PointInfo) {
            binding.run {
                if (pointModel.challengePointStatuses.isNotEmpty() && adapterPosition < pointModel.challengePointStatuses.size) {
                    when (pointModel.challengePointStatuses[adapterPosition].status) {
                        PointInfo.GetPointStatus.EARNED -> {
                            tvPointButton.isEnabled = false
                            tvPointButton.background =
                                context.getDrawable(R.drawable.point_button_background_already_get)
                            val textColor = ContextCompat.getColor(
                                context,
                                com.hmh.hamyeonham.core.designsystem.R.color.blue_purple_opacity_70
                            )
                            tvPointButton.setTextColor(textColor)
                        }

                        PointInfo.GetPointStatus.UNEARNED -> {
                            tvPointButton.isEnabled = true
                            tvPointButton.setOnClickListener {
                                onButtonClick(pointModel.challengePointStatuses[adapterPosition].challengeDate)
                            }
                        }

                        PointInfo.GetPointStatus.FAILURE -> {
                            tvPointButton.isEnabled = false
                            tvPointButton.background =
                                context.getDrawable(R.drawable.point_button_background_disable)
                            val textColor = ContextCompat.getColor(
                                context,
                                com.hmh.hamyeonham.core.designsystem.R.color.gray2
                            )
                            tvPointButton.setTextColor(textColor)
                        }

                        PointInfo.GetPointStatus.NONE -> {
                            binding.root.visibility = android.view.View.GONE
                        }
                    }
                } else {
                    tvPointButton.isEnabled = false
                    tvPointButton.background =
                        context.getDrawable(R.drawable.point_button_background_disable)
                    val textColor = ContextCompat.getColor(
                        context,
                        com.hmh.hamyeonham.core.designsystem.R.color.gray2
                    )
                    tvPointButton.setTextColor(textColor)
                }
            }
        }
    }
}
