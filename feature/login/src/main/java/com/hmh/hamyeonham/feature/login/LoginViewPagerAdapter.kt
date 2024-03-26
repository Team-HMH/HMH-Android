package com.hmh.hamyeonham.feature.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.hmh.hamyeonham.common.view.ItemDiffCallback
import com.hmh.hamyeonham.feature.login.databinding.ItemLoginViewPagerBinding

class LoginViewPagerAdapter(private val imageList: List<Int>) :
    ListAdapter<Int, LoginViewPagerAdapter.PagerViewHolder>(
        ItemDiffCallback(
            onItemsTheSame = { oldItem, newItem -> oldItem == newItem },
            onContentsTheSame = { oldItem, newItem -> oldItem == newItem },
        ),
    ),
    ViewPager2.PageTransformer {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_login_view_pager, parent, false)
        return PagerViewHolder(ItemLoginViewPagerBinding.bind(view))
    }

    class PagerViewHolder(private val binding: ItemLoginViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBindView(imageInfo: Int) {
            binding.ivLoginViewPagerItem.load(imageInfo) {
                placeholder(imageInfo)
                error(imageInfo)
            }
        }
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val exploreImage = imageList[position]
        holder.onBindView(exploreImage)
    }

    override fun transformPage(view: View, position: Float) {
        view.alpha = 1 - kotlin.math.abs(position)
    }
}
