package com.hmh.hamyeonham.feature.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hmh.hamyeonham.feature.onboarding.fragment.OnBoardingAppAddSelectionFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.OnBoardingRequestPermissionFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.OnBoardingSelectAppFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.OnBoardingSelectDataFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.OnBoardingSelectScreenTimeFragment
import com.hmh.hamyeonham.feature.onboarding.fragment.OnBoardingSelectUseTimeFragment

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
            OnBoardingFragmentType.SELECT_DATA_TIME -> OnBoardingSelectDataFragment.newInstance(fragmentType)

            OnBoardingFragmentType.SELECT_DATA_PROBLEM -> OnBoardingSelectDataFragment.newInstance(
                fragmentType
            )

            OnBoardingFragmentType.SELECT_DATA_PERIOD -> OnBoardingSelectDataFragment.newInstance(
                fragmentType
            )

            OnBoardingFragmentType.SELECT_SCREEN_TIME_GOAL -> OnBoardingSelectScreenTimeFragment()
            OnBoardingFragmentType.REQUEST_PERMISSION -> OnBoardingRequestPermissionFragment()
            OnBoardingFragmentType.SELECT_APP -> OnBoardingSelectAppFragment()
            OnBoardingFragmentType.SELECT_APP_VIEW -> OnBoardingAppAddSelectionFragment()
            OnBoardingFragmentType.SELECT_USE_TIME_GOAL -> OnBoardingSelectUseTimeFragment()
            else -> OnBoardingSelectUseTimeFragment()
        }
    }
}