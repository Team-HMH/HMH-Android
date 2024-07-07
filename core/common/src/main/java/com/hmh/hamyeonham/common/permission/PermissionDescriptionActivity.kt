package com.hmh.hamyeonham.common.permission

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hmh.hamyeonham.common.databinding.ActivityPermissionDescriptionBinding
import com.hmh.hamyeonham.common.view.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionDescriptionActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPermissionDescriptionBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setButtonClickListener()
    }

    private fun setButtonClickListener() {
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, PermissionActivity::class.java)
            startActivity(
                intent
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
            finish()
        }
        binding.ivBack.setOnClickListener { finish() }
    }
}
