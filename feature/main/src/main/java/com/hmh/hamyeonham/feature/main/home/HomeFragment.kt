package com.hmh.hamyeonham.feature.main.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.main.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val activityViewModel by activityViewModels<MainViewModel>()

    //    val mediaPlayer = MediaPlayer()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStaticsRecyclerView()
        collectUsageStatsList()
        setBlackholeLoop()
    }

    private fun setBlackholeLoop() {
        binding.vvBlackhole.setOnCompletionListener {
            binding.vvBlackhole.start()
        }
    }

    private fun initStaticsRecyclerView() {
        binding.rvStatics.run {
            adapter = UsageStaticsAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectUsageStatsList() {
        val usageStaticsAdapter = binding.rvStatics.adapter as? UsageStaticsAdapter
        activityViewModel.mainState.flowWithLifecycle(viewLifeCycle).onEach {
            usageStaticsAdapter?.submitList(it.usageStatsList)
            if (it.usageStatsList.isNotEmpty()) {
                Log.d("package name", requireActivity().packageName)
                Log.d("uri", activityViewModel.getBlackholeUri())

                bindBlackholeVideo(activityViewModel.getBlackholeUri())
                bindBlackholeDescription(activityViewModel.getBlackholeDescription())
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun bindBlackholeVideo(uri: String) {
        val uri =
            Uri.parse(uri)
        binding.vvBlackhole.run {
            setVideoURI(uri)
            requestFocus()
            start()
        }
    }

    private fun bindBlackholeDescription(description: String) {
        binding.tvHmhTitle.text = description
    }

    override fun onResume() {
        super.onResume()
        binding.vvBlackhole.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.vvBlackhole.pause()
    }
}
