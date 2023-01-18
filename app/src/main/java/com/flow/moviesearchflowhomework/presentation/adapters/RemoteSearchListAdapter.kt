package com.flow.moviesearchflowhomework.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flow.moviesearchflowhomework.BR
import com.flow.moviesearchflowhomework.databinding.ItemSearchResultBinding
import com.flow.moviesearchflowhomework.domain.entity.SearchItem

class RemoteSearchListAdapter(val listener: (SearchItem) -> Unit) :
    ListAdapter<SearchItem, RemoteSearchListAdapter.SearchViewHolder>(
        object : DiffUtil.ItemCallback<SearchItem>() {
            override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean =
                oldItem.link == newItem.link

            override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean =
                oldItem == newItem
        }
    ) {
    private lateinit var inflater: LayoutInflater


    class SearchViewHolder(val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return SearchViewHolder(ItemSearchResultBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.setVariable(BR.searchList, data)
        holder.binding.root.setOnClickListener {
            listener(data)
        }
    }
}