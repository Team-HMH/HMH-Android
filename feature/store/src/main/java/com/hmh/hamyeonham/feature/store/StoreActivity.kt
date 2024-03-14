package com.hmh.hamyeonham.feature.store

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.store.databinding.ActivityStoreBinding

class StoreActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityStoreBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
