package com.hmh.hamyeonham.feature.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hmh.hamyeonham.feature.onboarding.fragment.AppAddSelectionFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.RequestPermissionFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.SelectAppFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.SelectDataFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.SelectScreenTimeFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.SelectUseTimeFragment

enum class OnBoardingFragmentType {
    SELECT_DATA_TIME,
    SELECT_DATA_PROBLEM,
    SELECT_DATA_PERIOD,
    REQUEST_PERMISSION,
    SELECT_APP,
    SELECT_APP_VIEW,
    SELECT_USE_TIME_GOAL,
    SELECT_SCREEN_TIME_GOAL;

    companion object {
        fun fromPosition(position: Int): OnBoardingFragmentType = entries.getOrNull(position) ?: SELECT_DATA_TIME
    }
}

class OnBoardingFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return OnBoardingFragmentType.entries.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (val fragmentType = OnBoardingFragmentType.fromPosition(position)) {
            OnBoardingFragmentType.SELECT_DATA_TIME -> SelectDataFragment.newInstance(fragmentType)

            OnBoardingFragmentType.SELECT_DATA_PROBLEM -> SelectDataFragment.newInstance(
                fragmentType
            )

            OnBoardingFragmentType.SELECT_DATA_PERIOD -> SelectDataFragment.newInstance(
                fragmentType
            )

            OnBoardingFragmentType.SELECT_SCREEN_TIME_GOAL -> SelectScreenTimeFragment()
            OnBoardingFragmentType.REQUEST_PERMISSION -> RequestPermissionFragment()
            OnBoardingFragmentType.SELECT_APP -> SelectAppFragment()
            OnBoardingFragmentType.SELECT_APP_VIEW -> AppAddSelectionFragment()
            OnBoardingFragmentType.SELECT_USE_TIME_GOAL -> SelectUseTimeFragment()
        }
    }
}