package com.hmh.hamyeonham.feature.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.main.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).root
    }
}
