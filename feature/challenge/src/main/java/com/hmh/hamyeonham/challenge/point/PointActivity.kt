package com.hmh.hamyeonham.challenge.point

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.ActivityPointBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PointActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPointBinding::inflate)
    private val viewModel by viewModels<PointViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppSelectionRecyclerAdapter()
        collectUserPoint()
    }

    private fun initAppSelectionRecyclerAdapter() {
        binding.rvPoint.run {
            adapter = PointAdapter(
                onButtonClick = {
                    // viewModel.earnChallengePoint(PointModel(challengeDate = ))
                },
                challengeStatus = ChallengeStatus()
            )
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun collectUserPoint() {
        viewModel.currentPointState.flowWithLifecycle(lifecycle)
            .onEach {
                binding.tvPointTotal.text = it.toString()
            }
            .launchIn(lifecycleScope)
    }
}
