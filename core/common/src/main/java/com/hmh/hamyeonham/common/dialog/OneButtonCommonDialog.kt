package com.hmh.hamyeonham.common.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hmh.hamyeonham.common.context.dialogWidthPercent
import com.hmh.hamyeonham.common.databinding.DialogCommonOneButtonBinding
import com.hmh.hamyeonham.common.view.setOnSingleClickListener
import com.hmh.hamyeonham.common.view.viewBinding

class OneButtonCommonDialog : DialogFragment() {

    private val binding by viewBinding(DialogCommonOneButtonBinding::bind)

    private var confirmButtonClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return DialogCommonOneButtonBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initButtonListener()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogWidthPercent(dialog)
        dialog?.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun setConfirmButtonClickListener(confirmButtonClickListener: () -> Unit) {
        this.confirmButtonClickListener = confirmButtonClickListener
    }

    private fun initViews() {
        val title = arguments?.getString(TITLE, "")
        val description = arguments?.getString(DESCRIPTION)
        val iconRes = arguments?.getInt(ICON_RES)
        val confirmButtonText = arguments?.getString(CONFIRM_BUTTON_TEXT, "")

        with(binding) {
            tvDialogTitle.text = title
            tvDialogDescription.text = description
            iconRes?.let { ivDialogIcon.setImageResource(it) }
            tvConfirmButton.text = confirmButtonText
        }
    }

    private fun initButtonListener() {
        binding.tvConfirmButton.setOnSingleClickListener {
            confirmButtonClickListener?.invoke()
            dismiss()
        }
    }

    fun showAllowingStateLoss(fm: FragmentManager, tag: String = "") {
        fm.beginTransaction().add(this, tag)
            .commitAllowingStateLoss()
    }

    companion object {
        const val TAG = "OneButtonCommonDialog"

        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val ICON_RES = "iconRes"
        const val CONFIRM_BUTTON_TEXT = "confirmButtonText"


        fun newInstance(
            title: String,
            description: String? = null,
            @DrawableRes iconRes: Int? = null,
            confirmButtonText: String,
        ): OneButtonCommonDialog {
            return OneButtonCommonDialog().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(DESCRIPTION, description)
                    iconRes?.let { putInt(ICON_RES, it) }
                    putString(CONFIRM_BUTTON_TEXT, confirmButtonText)
                }
            }
        }
    }
}