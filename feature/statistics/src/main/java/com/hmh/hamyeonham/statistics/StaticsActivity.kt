package com.hmh.hamyeonham.statistics

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.statistics.databinding.ActivityStaticsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StaticsActivity : AppCompatActivity() {
    private val staticsViewModel by viewModels<StaticsViewModel>()
    private val binding by viewBinding(ActivityStaticsBinding::inflate)

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        collectUsageStatsList()
    }

    private fun collectUsageStatsList() {
        val usageStaticsAdapter = UsageStaticsAdapter()
        binding.rvStatics.run {
            adapter = usageStaticsAdapter
            layoutManager = LinearLayoutManager(this@StaticsActivity)
        }
        lifecycleScope.launch {
            staticsViewModel.usageStatAndGoalList.collect {
                usageStaticsAdapter.submitList(it)
            }
        }
    }
}
