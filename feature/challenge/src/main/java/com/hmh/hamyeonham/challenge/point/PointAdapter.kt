package com.hmh.hamyeonham.challenge.point

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemPointBinding

class PointAdapter(
    private val onButtonClick: (PointModel) -> Unit,
    private val challengeStatus: ChallengeStatus,
) : ListAdapter<PointModel, PointAdapter.PointViewHolder>(
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
            challengeStatus
        )
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class PointViewHolder(
        private val binding: ItemPointBinding,
        private val onButtonClick: (PointModel) -> Unit,
        private val challengeStatus: ChallengeStatus,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun onBind(pointModel: PointModel) {


            binding.run {
                tvPointTitle.text = itemView.context.getString(
                    R.string.tv_point_title,
                    pointModel.pointTitle
                )
                tvPointWhatChallenge.text =
                    itemView.context.getString(
                        R.string.tv_point_what_challenge,
                        challengeStatus.period.toString()
                    )
                tvPointButton.text =
                    itemView.context.getString(R.string.point_point, pointModel.point.toString())

                when (pointModel.getPointStatus) {
                    PointModel.GetPointStatus.ALREADY_GET_POINT -> {
                        tvPointButton.isEnabled = false
                        tvPointButton.background =
                            context.getDrawable(R.drawable.point_button_background_already_get)
                        val textColor = ContextCompat.getColor(
                            context,
                            com.hmh.hamyeonham.core.designsystem.R.color.blue_purple_opacity_70
                        )
                        tvPointButton.setTextColor(textColor)
                    }

                    PointModel.GetPointStatus.CAN_GET_POINT -> {
                        tvPointButton.isEnabled = true
                        onButtonClick(pointModel)
                    }

                    PointModel.GetPointStatus.FAIL_CHALLENGE -> {
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
}
