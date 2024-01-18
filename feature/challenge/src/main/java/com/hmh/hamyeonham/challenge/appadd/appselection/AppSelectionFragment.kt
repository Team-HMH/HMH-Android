package com.hmh.hamyeonham.challenge.appadd.appselection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.challenge.appadd.AppAddViewModel
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.FrargmentAppSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AppSelectionFragment : Fragment() {
    private val binding by viewBinding(FrargmentAppSelectionBinding::bind)
    private val viewModel by viewModels<AppSelectionViewModel>()
    private val activityViewModel by activityViewModels<AppAddViewModel>()

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
        initSearchBar()
        collectState()
        viewModel.getInstalledApps()
    }

    private fun initViews() {
        initAppSelectionRecyclerAdapter()
    }

    private fun initSearchBar() {
        binding.etSearchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setRecyclerViewWithFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setRecyclerViewWithFilter(filter: String) {
        val appSelectionAdapter = binding.rvAppSelection.adapter as? AppSelectionAdapter
        val selectedApp = viewModel.state.value.selectedApp
        val newAppList = selectedApp.filter {
            (context?.getAppNameFromPackageName(it) ?: "").contains(filter)
        }
        appSelectionAdapter?.submitList(
            newAppList.map {
                Pair(
                    it,
                    checkIfAppIsSelected(it),
                )
            },
        )
    }

    private fun checkIfAppIsSelected(app: String): Boolean {
        val selcetedApps = activityViewModel.state.value.selectedApp
        val isChecked = selcetedApps.find { it == app } != null
        if (isChecked) Log.d("checked app", app)
        return isChecked
    }

    private fun collectState() {
        viewModel.state.flowWithLifecycle(lifecycle).onEach { state ->
            val appSelectionAdapter = binding.rvAppSelection.adapter as? AppSelectionAdapter
            appSelectionAdapter?.submitList(
                state.selectedApp.map {
                    Pair(
                        it,
                        checkIfAppIsSelected(it),
                    )
                },
            )
        }.launchIn(lifecycleScope)
    }

    private fun initAppSelectionRecyclerAdapter() {
        binding.rvAppSelection.run {
            adapter = AppSelectionAdapter(
                onAppCheckboxClicked = ::onAppCheckboxClicked,
                onAppCheckboxUnClicked = ::onAppCheckboxUnClicked,
            )
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onAppCheckboxClicked(packageName: String) {
        activityViewModel.updateState {
            copy(selectedApp = selectedApp + packageName)
        }
        for (i in activityViewModel.state.value.selectedApp)
            Log.d("selected apps", i)
    }

    private fun onAppCheckboxUnClicked(packageName: String) {
        activityViewModel.updateState {
            copy(selectedApp = selectedApp - packageName)
        }
        Log.d("delete viewmodel", "")
        for (i in activityViewModel.state.value.selectedApp)
            Log.d("selected apps", i)
    }
}
