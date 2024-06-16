package com.hmh.hamyeonham.challenge.point

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.domain.point.model.PointInfo
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ItemPointBinding

class PointAdapter(
    private val onButtonClick: (String) -> Unit,
) : ListAdapter<PointInfo.ChallengePointStatus, PointAdapter.PointViewHolder>(
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

        fun onBind(pointModel: PointInfo.ChallengePointStatus) {
            binding.run {

                tvPointTitle.text =
                    context.getString(R.string.tv_point_title, (adapterPosition + 1).toString())
                tvPointWhatChallenge.text =
                    context.getString(
                        R.string.tv_point_what_challenge,
                        pointModel.period.toString()
                    )
                tvPointButton.text =
                    context.getString(R.string.point_point, pointModel.challengePoint.toString())

                setPointButtonStatus(pointModel)
            }
        }

        private fun ItemPointBinding.setPointButtonStatus(pointModel: PointInfo.ChallengePointStatus) {

            val textColorNone = ContextCompat.getColor(
                context,
                com.hmh.hamyeonham.core.designsystem.R.color.gray3
            )
            val textColorEarned = ContextCompat.getColor(
                context,
                com.hmh.hamyeonham.core.designsystem.R.color.blue_purple_opacity_70
            )

            val textColorTitle = ContextCompat.getColor(
                context,
                com.hmh.hamyeonham.core.designsystem.R.color.white_text
            )

            val textColorUnearned = ContextCompat.getColor(
                context,
                com.hmh.hamyeonham.core.designsystem.R.color.white_btn
            )

            val textColorSubscription = ContextCompat.getColor(
                context,
                com.hmh.hamyeonham.core.designsystem.R.color.gray2
            )



            val textColorFailure = ContextCompat.getColor(
                context,
                com.hmh.hamyeonham.core.designsystem.R.color.gray2
            )

            val buttonBackgroundEarned = AppCompatResources.getDrawable(
                context,
                R.drawable.point_button_background_already_get
            )

            val buttonBackgroundUnearned = AppCompatResources.getDrawable(
                context,
                R.drawable.point_button_background_able
            )

            val buttonBackgroundFailure = AppCompatResources.getDrawable(
                context,
                R.drawable.point_button_background_failure
            )

            val buttonBackgroundNone = AppCompatResources.getDrawable(
                context,
                R.drawable.point_button_background_none
            )

            when (pointModel.status) {
                // 이미 받은 경우
                PointInfo.GetPointStatus.EARNED -> {
                    setTextColor(textColorTitle, textColorSubscription)
                    tvPointButton.isEnabled = false
                    tvPointButton.background = buttonBackgroundEarned
                    tvPointButton.setTextColor(textColorEarned)
                }
                // 받을 수 있는 경우
                PointInfo.GetPointStatus.UNEARNED -> {
                    tvPointTitle.setTextColor(textColorTitle)
                    tvPointWhatChallenge.setTextColor(textColorSubscription)
                    tvPointButton.isEnabled = true
                    tvPointButton.background = buttonBackgroundUnearned
                    tvPointButton.setTextColor(textColorUnearned)
                    tvPointButton.setOnClickListener {
                        onButtonClick(pointModel.challengeDate)
                    }
                }
                // 챌린지 실패로 인한 포인트 획득 불가
                PointInfo.GetPointStatus.FAILURE -> {
                    tvPointTitle.setTextColor(textColorTitle)
                    tvPointWhatChallenge.setTextColor(textColorSubscription)
                    tvPointButton.isEnabled = false
                    tvPointButton.background = buttonBackgroundFailure
                    tvPointButton.setTextColor(textColorFailure)
                }
                // 아직 챌린지를 시도하지 않은 날짜
                PointInfo.GetPointStatus.NONE -> {
                    tvPointTitle.setTextColor(textColorNone)
                    tvPointWhatChallenge.setTextColor(textColorNone)
                    tvPointButton.isEnabled = false
                    tvPointButton.background = buttonBackgroundNone
                    tvPointButton.setTextColor(textColorNone)
                }
            }
        }

        private fun ItemPointBinding.setTextColor(
            textColorTitle: Int,
            textColorSubscription: Int
        ) {
            tvPointTitle.setTextColor(textColorTitle)
            tvPointWhatChallenge.setTextColor(textColorSubscription)
        }
    }
}

