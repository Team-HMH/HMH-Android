package com.hmh.hamyeonham.feature.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.store.databinding.ActivityStoreBinding

class StoreActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityStoreBinding::inflate)

    override fun onResume() {
        super.onResume()
        AmplitudeUtils.trackEventWithProperties("view_shop")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initStoreButton()
    }

    private fun initStoreButton() {
        binding.tvStore.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}
