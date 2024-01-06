 package com.hmh.hamyeonham.statistics

 import android.content.Context
 import androidx.recyclerview.widget.RecyclerView
 import com.hmh.hamyeonham.common.time.convertTimeToString
 import com.hmh.hamyeonham.feature.statistics.databinding.ItemUsagestaticTotalBinding
 import com.hmh.hamyeonham.usagestats.model.UsageStatAndGoal

 class UsageStaticsTotalViewHolder(
    private val binding: ItemUsagestaticTotalBinding,
    private val context: Context,
 ) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(usageStatAndGoal: UsageStatAndGoal) {
        binding.run {
            tvStaticsTotalHour.text = convertTimeToString(usageStatAndGoal.goalTime)
            pbStaticsTotal.progress = usageStatAndGoal.usedPercentage
            tvStaticsTotalTimeLeft.text = convertTimeToString(usageStatAndGoal.timeLeft)
        }
    }
 }
