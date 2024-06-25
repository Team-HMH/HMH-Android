package com.hmh.hamyeonham.challenge.appadd.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.challenge.appadd.AppAddViewModel
import com.hmh.hamyeonham.common.view.setupScreentimeGoalRange
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.FragmentSetGoalTimeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetGoalTimeFragment : Fragment() {
    private val binding by viewBinding(FragmentSetGoalTimeBinding::bind)
    private val viewModel by activityViewModels<AppAddViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentSetGoalTimeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setNumberPicker()
        setNumberPickerListener()
    }

    private fun initViews() {
        viewModel.handleNextButtonStateWithGoalTime()
    }

    private fun setNumberPickerListener() {
        binding.npUseTimeGoalHour.setOnValueChangedListener { _, _, newTime ->
            viewModel.setGoalHour((newTime * 60 * 60 * 1000).toLong())
        }
        binding.npUseTimeGoalMinute.setOnValueChangedListener { _, _, newTime ->
            viewModel.setGoalMin((newTime * 60 * 1000).toLong())
        }
    }

    private fun setNumberPicker() {
        binding.run {
            npUseTimeGoalHour.setupScreentimeGoalRange(0, 1)
            npUseTimeGoalMinute.setupScreentimeGoalRange(0, 59)
            npUseTimeGoalHour.descendantFocusability =
                NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }
    }
}
