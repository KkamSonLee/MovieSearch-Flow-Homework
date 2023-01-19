package com.flow.moviesearchflowhomework.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.flow.moviesearchflowhomework.BR
import com.flow.moviesearchflowhomework.databinding.ItemRecentKeywordBinding
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.presentation.util.setOnSingleClickListener

class RecentSearchListAdapter(
    val recentKeywordClickListener: (RecentSearchKeywordEntity) -> Unit,
    val deleteKeywordClickListener: (RecentSearchKeywordEntity) -> Unit
) :
    ListAdapter<RecentSearchKeywordEntity, RecentSearchListAdapter.RecentSearchViewHolder>(
        object : DiffUtil.ItemCallback<RecentSearchKeywordEntity>() {
            override fun areItemsTheSame(
                oldItem: RecentSearchKeywordEntity,
                newItem: RecentSearchKeywordEntity
            ): Boolean =
                oldItem.keyword == newItem.keyword

            override fun areContentsTheSame(
                oldItem: RecentSearchKeywordEntity,
                newItem: RecentSearchKeywordEntity
            ): Boolean =
                oldItem == newItem
        }) {

    private lateinit var inflater: LayoutInflater

    class RecentSearchViewHolder(val binding: ItemRecentKeywordBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {

        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return RecentSearchViewHolder(ItemRecentKeywordBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.setVariable(BR.recentItem, data)
        holder.binding.root.setOnSingleClickListener {
            recentKeywordClickListener(data)
        }
        holder.binding.ivKeywordDelete.setOnSingleClickListener {
            deleteKeywordClickListener(data)
        }
    }
}