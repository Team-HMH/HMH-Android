package com.hmh.hamyeonham.feature.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.hmh.hamyeonham.feature.login.databinding.ItemLoginViewPagerBinding

class LoginViewPagerAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<LoginViewPagerAdapter.PagerViewHolder>(),
    ViewPager2.PageTransformer {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_login_view_pager, parent, false)
        return PagerViewHolder(ItemLoginViewPagerBinding.bind(view))
    }

    class PagerViewHolder(private val binding: ItemLoginViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBindView(imageInfo: Int) {
            binding.run {
                ivLoginViewPagerItem.load(imageInfo) {
                    placeholder(R.drawable.load_fail)
                    error(R.drawable.load_fail)
                }
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
