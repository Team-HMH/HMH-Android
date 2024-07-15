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
import com.hmh.hamyeonham.feature.onboarding.fragment.OnBoardingSpecifyPermissionFragment

enum class OnBoardingFragmentType(val position: Int) {
    SELECT_DATA_TIME(0),
    SELECT_DATA_PROBLEM(1),
    SELECT_DATA_PERIOD(2),
    SPECIFY_PERMISSION(3),
    REQUEST_PERMISSION(4),
    SELECT_APP(5),
    SELECT_APP_VIEW(6),
    SELECT_USE_TIME_GOAL(7),
    SELECT_SCREEN_TIME_GOAL(8);

    companion object {
        private val map = entries.associateBy(OnBoardingFragmentType::position)
        fun fromPosition(position: Int) = map[position]
    }
}

class OnBoardingFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return OnBoardingFragmentType.entries.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (val fragmentType = OnBoardingFragmentType.fromPosition(position)) {
            OnBoardingFragmentType.SELECT_DATA_TIME -> OnBoardingSelectDataFragment.newInstance(
                fragmentType
            )

            OnBoardingFragmentType.SELECT_DATA_PROBLEM -> OnBoardingSelectDataFragment.newInstance(
                fragmentType
            )

            OnBoardingFragmentType.SELECT_DATA_PERIOD -> OnBoardingSelectDataFragment.newInstance(
                fragmentType
            )

            OnBoardingFragmentType.SELECT_SCREEN_TIME_GOAL -> OnBoardingSelectScreenTimeFragment()
            OnBoardingFragmentType.REQUEST_PERMISSION -> OnBoardingRequestPermissionFragment()
            OnBoardingFragmentType.SPECIFY_PERMISSION -> OnBoardingSpecifyPermissionFragment()
            OnBoardingFragmentType.SELECT_APP -> OnBoardingSelectAppFragment()
            OnBoardingFragmentType.SELECT_APP_VIEW -> OnBoardingAppAddSelectionFragment()
            OnBoardingFragmentType.SELECT_USE_TIME_GOAL -> OnBoardingSelectUseTimeFragment()
            else -> OnBoardingSelectUseTimeFragment()
        }
    }
}