package com.hmh.hamyeonham.challenge.newchallenge

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hmh.hamyeonham.challenge.newchallenge.periodselection.PeriodSelectionFragment
import com.hmh.hamyeonham.challenge.newchallenge.timeselection.TimeSelectionFragment

enum class FRAGMENT(val position: Int) {
    PERIODSELECTION(0),
    TIMESELECTION(1)
}

class NewChallengeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FRAGMENT.PERIODSELECTION.position -> PeriodSelectionFragment()
            else -> TimeSelectionFragment()
        }
    }
}
