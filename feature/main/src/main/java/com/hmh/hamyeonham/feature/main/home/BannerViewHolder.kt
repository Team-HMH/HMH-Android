package com.hmh.hamyeonham.feature.main.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hmh.hamyeonham.common.context.toast
import com.hmh.hamyeonham.common.image.setBackgroundColors
import com.hmh.hamyeonham.core.viewmodel.HomeItem
import com.hmh.hamyeonham.feature.main.R
import com.hmh.hamyeonham.feature.main.databinding.ItemBannerBinding

class BannerViewHolder(
    private val binding: ItemBannerBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: HomeItem.BannerModel) {
        val context = binding.root.context
        binding.run {
            ivImage.load(item.imageUrl)
            tvTitle.text = item.title
            tvSubTitle.text = item.subTitle

            ivBackground.setBackgroundColors(item.backgroundColors)
        }
        binding.root.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.linkUrl))
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val errorMessage = context.getString(R.string.error_activity_not_found)
                context.toast(errorMessage)
            }
        }
    }
}
