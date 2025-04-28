package com.yahoraustudio.rlottie_poc.grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.yahoraustudio.rlottie_poc.R
import com.yahoraustudio.rlottie_poc.RLottieImageView

class GridAdapter : ListAdapter<Int, GridAdapter.IntViewHolder>(DiffCallback) {

    class IntViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = RLottieImageView(itemView.context).apply {
            tag = "RLottieImageView"
        }

        init {
            // add view
            val container = itemView.findViewById<FrameLayout>(R.id.container)

            if (container.findViewWithTag<RLottieImageView>("RLottieImageView") == null) {
                container.addView(imageView)
                imageView.updateLayoutParams {
                    width = MATCH_PARENT
                    height = MATCH_PARENT
                }
            }
        }

        fun bind(resId: Int) {
            imageView.setAutoRepeat(true)
            imageView.setAnimation(resId, 150, 150, null)
            imageView.playAnimation()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return IntViewHolder(view)
    }

    override fun onBindViewHolder(holder: IntViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: IntViewHolder) {
        super.onViewRecycled(holder)
        holder.imageView.stopAnimation()
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                // If you had an ID you could compare IDs, here just compare values
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }
        }
    }
}