package com.seiko.tv.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seiko.tv.databinding.ItemBangumiSeasonBinding
import com.seiko.tv.util.diff.BangumiSeasonDiffCallback
import com.seiko.tv.data.model.api.BangumiSeason
import com.seiko.common.ui.adapter.BaseListAdapter

class BangumiSeasonAdapter : BaseListAdapter<BangumiSeason, BangumiSeasonAdapter.ViewHolder>(BangumiSeasonDiffCallback()) {

    companion object {
        private const val ARGS_SELECT_POSITION = "ARGS_SELECT_POSITION"
    }

    private var selectPosition = 0

//    var items: List<BangumiSeason> = emptyList()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    fun get(position: Int): BangumiSeason? {
        if (position < 0 || position >= itemCount) return null
        return getItem(position)
    }

//    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBangumiSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onPayload(holder: ViewHolder, bundle: Bundle) {
        holder.payload(bundle)
    }

    /**
     * 当前选中的番剧
     */
    fun setSelectPosition(position: Int) {
        if (selectPosition == position) {
            return
        }
        if (selectPosition >= 0) {
            notifyItemChanged(selectPosition, Bundle().apply { putBoolean(ARGS_SELECT_POSITION, false) })
        }
        if (position >= 0) {
            notifyItemChanged(position, Bundle().apply { putBoolean(ARGS_SELECT_POSITION, true) })
        }
        selectPosition = position
    }

    inner class ViewHolder(
        private val binding: ItemBangumiSeasonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.isFocusable = true
            binding.root.isFocusableInTouchMode = true
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != -1) {
                    listener?.onClick(this, getItem(position), position)
                }
            }
        }

        fun bind(position: Int) {
            val item = getItem(position)
            binding.title.text = item.seasonName
            binding.root.isSelected = selectPosition == position
        }

        fun payload(bundle: Bundle) {
            if (bundle.containsKey(BangumiSeasonDiffCallback.ARGS_ANIME_TITLE)) {
                binding.title.text = bundle.getString(BangumiSeasonDiffCallback.ARGS_ANIME_TITLE)
            }
            if (bundle.containsKey(ARGS_SELECT_POSITION)) {
                itemView.isSelected = bundle.getBoolean(ARGS_SELECT_POSITION)
            }
        }
    }
}