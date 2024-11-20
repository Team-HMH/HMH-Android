package com.hmh.hamyeonham.challenge.point

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.common.context.toast
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.ActivityPointBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PointActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPointBinding::inflate)
    private val viewModel by viewModels<PointViewModel>()

    override fun onResume() {
        super.onResume()
        AmplitudeUtils.trackEventWithProperties("view_point")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        collectPointInfo()
        collectUserPoint()
        collectPointSuccessState()
    }

    private fun initViews() {
        initAdapter()
        initCloseClickListener()
    }

    private fun initAdapter() {
        val adapter = PointAdapter(
            onButtonClick = { challengeDate ->
                viewModel.earnChallengePoint(challengeDate)
            },
        )
        binding.rvPoint.run {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initCloseClickListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun collectPointInfo() {
        viewModel.pointInfoList.flowWithLifecycle(lifecycle).onEach { pointInfoList ->
            (binding.rvPoint.adapter as? PointAdapter)?.submitList(pointInfoList)
        }.launchIn(lifecycleScope)
    }

    private fun collectUserPoint() {
        viewModel.currentPointState.flowWithLifecycle(lifecycle).onEach {
            binding.tvPointTotal.text = it.toString()
            setResult(RESULT_OK, intent.putExtra("point", it))
        }.launchIn(lifecycleScope)
    }

    private fun collectPointSuccessState() {
        viewModel.getPointSuccess.flowWithLifecycle(lifecycle).onEach {
            if(it) {
                toast("포인트를 획득했어요!")
            }
        }.launchIn(lifecycleScope)
    }
}
