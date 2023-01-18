package com.flow.moviesearchflowhomework.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.flow.moviesearchflowhomework.databinding.ActivityMovieSearchBinding
import com.flow.moviesearchflowhomework.domain.entity.SearchItem
import com.flow.moviesearchflowhomework.presentation.adapters.RemoteSearchListAdapter
import com.flow.moviesearchflowhomework.presentation.util.*
import com.flow.moviesearchflowhomework.presentation.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSearchActivity :
    BaseActivity<ActivityMovieSearchBinding>(ActivityMovieSearchBinding::inflate) {
    private lateinit var searchAdapter: RemoteSearchListAdapter
    private val searchViewModel by viewModels<SearchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        setAdapter()
        applyListener()
        collectSearchData()
    }

    private fun collectSearchData() {
        collectFlowWhenStarted(searchViewModel.searchData) { searchItems ->
            when (searchItems) {
                is UiState.Init -> {}
                is UiState.Success -> {
                    onSuccess(searchItems)
                }
                is UiState.Loading -> {
                    onLoad()
                }
                is UiState.Error -> {
                    showToast("Something to wrong")
                }
            }
        }
    }

    private fun onSuccess(searchItems: UiState.Success<List<SearchItem>>) {
        binding.pbProgress.setGone()
        binding.tvViewRecentSearch.isClickable = true
        searchAdapter.isClickable = true
        searchAdapter.submitList(searchItems.data)
    }

    private fun onLoad() {
        binding.pbProgress.setVisible()
        binding.tvViewRecentSearch.isClickable = false
        searchAdapter.isClickable = false
    }

    private fun setAdapter() {
        searchAdapter = RemoteSearchListAdapter { item ->
            Intent(this, SearchDetailActivity::class.java).apply {
                putExtra(LINK_NAME, item.link)
                startActivity(this)
            }
        }
        binding.rvSearchList.adapter = searchAdapter
    }

    private fun applyListener() {
        binding.tvViewRecentSearch.setOnClickListener {
            showToast("Hi Touch")
        }
        binding.etSearch.setOnEditorActionListener { edit, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.callSearch(edit.text.toString())
            }
            false
        }
    }

    companion object {
        const val LINK_NAME = "link"
    }
}