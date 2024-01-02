package com.hmh.hamyeonham

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.databinding.ItemUsagestaticBinding
import com.hmh.hamyeonham.usagestats.model.UsageStat

class UsageStaticsViewHolder(private val binding: ItemUsagestaticBinding, private val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(usageStat: UsageStat) {
        binding.tvItemusagestatAppname.text = context.getAppNameFromPackageName(usageStat.packageName)
        binding.tvItemusagestatLeftHour.text = usageStat.totalTimeInForeground.toString()
        binding.ivItemusagestatAppicon.setImageDrawable(context.getAppIconFromPackageName(usageStat.packageName))
        binding.pbItemUsagestat.progress = getUsedPercentage(usageStat.totalTimeInForeground)
    }

    fun getUsedPercentage(usedTime: Long): Int {
        val usedMin = convertMsToMin(usedTime)
        val goal = 60
        return usedMin / goal
    }

    private fun convertMsToMin(ms: Long) = (ms / (1000 * 60)).toInt()
}
