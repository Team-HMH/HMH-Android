package com.hmh.hamyeonham.challenge.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus
import com.hmh.hamyeonham.feature.challenge.databinding.ItemChallengeStatusBinding

class ChallengeCalendarAdapter(private val context: Context) :
    RecyclerView.Adapter<ChallengeStatusViewHolder>() {

    private val items = mutableListOf<ChallengeStatus>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeStatusViewHolder {
        return ChallengeStatusViewHolder(
            ItemChallengeStatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ), context
        )
    }

    override fun onBindViewHolder(holder: ChallengeStatusViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<ChallengeStatus>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged() // 애니메이션 없이 갱신
    }
}
