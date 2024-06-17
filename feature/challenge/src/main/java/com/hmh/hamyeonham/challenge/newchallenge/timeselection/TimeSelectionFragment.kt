package com.hmh.hamyeonham.challenge.newchallenge.timeselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.challenge.newchallenge.NewChallengeViewModel
import com.hmh.hamyeonham.common.time.hourToMs
import com.hmh.hamyeonham.common.view.setupScreentimeGoalRange
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.FragmentTimeSelectionBinding

class TimeSelectionFragment : Fragment() {
    private val binding by viewBinding(FragmentTimeSelectionBinding::bind)
    private val viewModel by activityViewModels<NewChallengeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentTimeSelectionBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNumberPicker()
    }

    private fun setNumberPicker() {
        binding.run {
            npNewChallengeScreentimeGoal.setupScreentimeGoalRange(MINTOTAL, MAXTOTAL)
            npNewChallengeScreentimeGoal.descendantFocusability =
                NumberPicker.FOCUS_BLOCK_DESCENDANTS
            npNewChallengeScreentimeGoal.setOnValueChangedListener { _, _, newTime ->
                viewModel.setGoalHour(newTime.hourToMs())
            }
        }
    }

    companion object {
        val MINTOTAL = 1
        val MAXTOTAL = 6
    }
}