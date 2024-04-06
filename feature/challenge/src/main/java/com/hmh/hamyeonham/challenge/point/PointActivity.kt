package com.hmh.hamyeonham.challenge.point

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.ActivityPointBinding

class PointActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPointBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppSelectionRecyclerAdapter()
    }

    private fun initAppSelectionRecyclerAdapter() {
        binding.rvPoint.run {
            val dummyPoints = dummy.getDummyPoints()
            val pointAdapter = PointAdapter()
            adapter = pointAdapter
            layoutManager = LinearLayoutManager(this@PointActivity)
            pointAdapter.submitList(dummyPoints)
        }
    }
}
