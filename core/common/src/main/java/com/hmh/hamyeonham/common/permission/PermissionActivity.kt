package com.hmh.hamyeonham.common.permission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.hmh.hamyeonham.common.databinding.ActivityPermissionBinding
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPermissionBinding::inflate)

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setScreen()
    }

    private fun setScreen() {
        val permissionFragment = navigationProvider.toPermission()
        supportFragmentManager.commit {
            replace(binding.fcvPermission.id, permissionFragment)
        }
    }
}