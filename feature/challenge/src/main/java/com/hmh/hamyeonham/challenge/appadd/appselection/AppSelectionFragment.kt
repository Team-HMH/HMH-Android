package com.hmh.hamyeonham.challenge.appadd.appselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.challenge.appadd.AppAddViewModel
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.FrargmentAppSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AppSelectionFragment : Fragment() {

    private val binding by viewBinding(FrargmentAppSelectionBinding::bind)
    private val viewModel by activityViewModels<AppAddViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FrargmentAppSelectionBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        collectState()
    }

    private fun initViews() {
        initAppSelectionRecyclerAdapter()
        initSearchBar()
    }

    private fun initSearchBar() {
        binding.etSearchbar.addTextChangedListener { text ->
            viewModel.onQueryChanged(text.toString())
        }
    }

    private fun collectState() {
        viewModel.state.flowWithLifecycle(viewLifeCycle).onEach { state ->
            val appSelectionAdapter = binding.rvAppSelection.adapter as? AppSelectionAdapter
            appSelectionAdapter?.submitList(state.getAppSelectionList(requireContext()))
        }.launchIn(viewLifeCycleScope)
    }

    private fun initAppSelectionRecyclerAdapter() {
        binding.rvAppSelection.run {
            adapter = AppSelectionAdapter(onAppCheckedChangeListener = viewModel::appCheckChanged)
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }
    }
}
