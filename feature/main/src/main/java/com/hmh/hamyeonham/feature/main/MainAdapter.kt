package com.hmh.hamyeonham.feature.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hmh.hamyeonham.challenge.ChallengeFragment
import com.hmh.hamyeonham.feature.main.home.HomeFragment
import com.hmh.hamyeonham.mypage.MyPageFragment

class MainAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = MainScreen.entries.size

    override fun createFragment(position: Int): Fragment {
        return when (MainScreen.fromPosition(position)) {
            MainScreen.CHALLENGE -> ChallengeFragment()
            MainScreen.HOME -> HomeFragment()
            MainScreen.MY_PAGE -> MyPageFragment()
        }
    }
}