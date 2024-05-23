package com.hmh.hamyeonham.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.dialog.TwoButtonCommonDialog
import com.hmh.hamyeonham.common.fragment.setShimmerVisibility
import com.hmh.hamyeonham.common.fragment.toast
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.mypage.R
import com.hmh.hamyeonham.feature.mypage.databinding.FragmentMyPageBinding
import com.hmh.hamyeonham.mypage.viewmodel.MyPageViewModel
import com.hmh.hamyeonham.mypage.viewmodel.UserEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private val binding by viewBinding(FragmentMyPageBinding::bind)
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<MyPageViewModel>()
    private lateinit var storeResultLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentMyPageBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            setShimmerVisibility(isLoading = true, binding.sflMypage, binding.svMypage)
            delay(3000)
            initViews()
            collectMainState()
            initStoreButton()
            initPrivacyButton()
            initTermOfUseButton()
            setShimmerVisibility(isLoading = false, binding.sflMypage, binding.svMypage)
        }

    }

    private fun initStoreButton() {
        binding.vStore.setOnClickListener {
            val intent = navigationProvider.toStore()
            startActivity(intent)
        }
    }

    private fun initViews() {
        binding.tvLogout.setOnClickListener {
            TwoButtonCommonDialog.newInstance(
                title = getString(R.string.logout_description),
                confirmButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_okay),
                dismissButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_cancel),
            ).apply {
                setConfirmButtonClickListener {
                    viewModel.handleLogout()
                    viewModel.deleteAllDatabase()
                    handleLogoutSuccess()
                }
                setDismissButtonClickListener {
                }
            }.showAllowingStateLoss(childFragmentManager)
        }

        binding.tvWithdrawal.setOnClickListener {
            TwoButtonCommonDialog.newInstance(
                title = getString(R.string.withdrawal_title),
                description = getString(R.string.withdrawal_description),
                confirmButtonText = getString(R.string.mypage_withdrawal),
                dismissButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_cancel),
            ).apply {
                setConfirmButtonClickListener {
                    viewModel.handleWithdrawal()
                    viewModel.deleteAllDatabase()
                    handleWithdrawalSuccess()
                }
                setDismissButtonClickListener {
                }
            }.showAllowingStateLoss(childFragmentManager)
        }
    }

    private fun handleLogoutSuccess() {
        viewModel.userEffect.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UserEffect.logoutSuccess -> moveToLoginActivity()
                is UserEffect.logoutFail -> toast(getString(R.string.logout_fail))
                else -> Unit
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleWithdrawalSuccess() {
        viewModel.userEffect.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UserEffect.withdrawalSuccess -> moveToLoginActivity()
                is UserEffect.withdrawalFail -> toast(getString(R.string.withdrawal_fail))
                else -> Unit
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun moveToLoginActivity() {
        val intent = navigationProvider.toLogin()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

    private fun collectMainState() {
        activityViewModel.mainState.flowWithLifecycle(viewLifeCycle).onEach {
            bindMyPageWithUserInfo(it.name, it.point)
            setShimmerVisibility(isLoading = false, binding.sflMypage, binding.svMypage)
        }.launchIn(viewLifeCycleScope)
    }

    private fun bindMyPageWithUserInfo(name: String, point: Int) {
        binding.run {
            tvUserName.text = name
            tvPoint.text = getString(R.string.mypage_point, point)
        }
    }

    private fun initPrivacyButton() {
        binding.vPrivacy.setOnClickListener {
            val privacyRuleUrl = getString(R.string.privacy_url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyRuleUrl))
            startActivity(intent)
        }
    }

    private fun initTermOfUseButton() {
        binding.vTermofuse.setOnClickListener {
            val termOfUseUrl = getString(R.string.term_of_use_url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termOfUseUrl))
            startActivity(intent)
        }
    }
}
