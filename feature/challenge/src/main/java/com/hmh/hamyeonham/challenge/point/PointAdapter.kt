package com.hmh.hamyeonham.challenge.point

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemPointBinding

class PointAdapter() : ListAdapter<PointModel, PointAdapter.PointViewHolder>(
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

            )
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class PointViewHolder(
        private val binding: ItemPointBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(pointModel: PointModel) {
            binding.run {
                tvPointTitle.text = itemView.context.getString(
                    R.string.tv_point_title,
                    pointModel.pointTitle
                )
                tvPointWhatChallenge.text =
                    itemView.context.getString(
                        R.string.tv_point_what_challenge,
                        pointModel.pointWhatChallenge
                    )
                tvPointButton.text =
                    itemView.context.getString(R.string.point_point, pointModel.point.toString())

                when (pointModel.getPointStatus) {
                    PointModel.GetPointStatus.ALREADY_GET_POINT -> {
                        tvPointButton.isEnabled = false
                        tvPointButton.background =
                            itemView.context.getDrawable(R.drawable.point_button_background_already_get)
                        val textColor = ContextCompat.getColor(
                            itemView.context,
                            com.hmh.hamyeonham.core.designsystem.R.color.blue_purple_opacity_70
                        )
                        tvPointButton.setTextColor(textColor)
                    }

                    PointModel.GetPointStatus.CAN_GET_POINT -> {
                        tvPointButton.isEnabled = true
                    }

                    PointModel.GetPointStatus.FAIL_CHALLENGE -> {
                        tvPointButton.isEnabled = false
                        tvPointButton.background =
                            itemView.context.getDrawable(R.drawable.point_button_background_disable)
                        val textColor = ContextCompat.getColor(
                            itemView.context,
                            com.hmh.hamyeonham.core.designsystem.R.color.gray2
                        )
                        tvPointButton.setTextColor(textColor)
                    }
                }
            }
        }
    }
}
