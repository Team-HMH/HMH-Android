package com.hmh.hamyeonham.challenge.appadd.appselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.challenge.appadd.AppAddViewModel
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.FrargmentAppSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppSelectionFragment : Fragment() {
    private val binding by viewBinding(FrargmentAppSelectionBinding::bind)
    private val viewModel by viewModels<AppSelectionViewModel>()
    private val activityViewModel by activityViewModels<AppAddViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FrargmentAppSelectionBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        initAppSelectionRecyclerAdapter()
    }

    private fun initAppSelectionRecyclerAdapter() {
        binding.rvAppSelection.run {
            adapter = AppSelectionAdapter(
                onAppCheckboxClicked = ::onAppCheckboxClicked,
                onAppCheckboxUnClicked = ::onAppCheckboxUnClicked,
            )
            layoutManager = LinearLayoutManager(requireContext())
        }
        setViewPager()
    }

    private fun setViewPager() {
        val appSelectionAdapter = binding.rvAppSelection.adapter as? AppSelectionAdapter
        appSelectionAdapter?.submitList(viewModel.getInstalledApps())
    }

    private fun onAppCheckboxClicked(packageName: String) {
        activityViewModel.updateState {
            copy(selectedApp = selectedApp + packageName)
        }
    }

    private fun onAppCheckboxUnClicked(packageName: String) {
        activityViewModel.updateState {
            copy(selectedApp = selectedApp - packageName)
        }
    }
}
