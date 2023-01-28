package com.flow.moviesearchflowhomework.presentation

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.flow.moviesearchflowhomework.databinding.ActivityMovieSearchBinding
import com.flow.moviesearchflowhomework.presentation.adapters.SearchListPagerAdapter
import com.flow.moviesearchflowhomework.presentation.util.BaseActivity
import com.flow.moviesearchflowhomework.presentation.util.collectFlowWhenStarted
import com.flow.moviesearchflowhomework.presentation.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSearchActivity :
    BaseActivity<ActivityMovieSearchBinding>(ActivityMovieSearchBinding::inflate) {
    private lateinit var searchAdapter: SearchListPagerAdapter
    private val searchViewModel by viewModels<SearchViewModel>()
    private val getResultText: ActivityResultLauncher<Intent> =    // Get Activity Result
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getStringExtra(SEARCH_KEYWORD).toString()
                searchViewModel.inputSearchText.value = data
                callSearch(data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.searchViewModel = searchViewModel
        setAdapter()
        setListener()
        collectFlowWhenStarted(searchViewModel.searchKeyword) { paging ->
            searchAdapter.submitData(paging)
        }
    }

    private fun callSearch(keyword: String) {      // Progress, Collect Paging List
        searchViewModel.addSearchKeyword(keyword)

        collectFlowWhenStarted(searchAdapter.loadStateFlow) { state ->
            binding.pbProgress.isVisible = state.refresh is LoadState.Loading
        }
        searchAdapter.addLoadStateListener { combinedLoadStates ->
            binding.pbProgress.isVisible = combinedLoadStates.refresh is LoadState.Loading
        }
    }

    private fun setAdapter() {
        searchAdapter = SearchListPagerAdapter { item ->     // Move to WebView
            Intent(this, SearchDetailActivity::class.java).apply {
                putExtra(LINK_NAME, item.link)
                startActivity(this)
            }
        }
        binding.rvSearchList.adapter = searchAdapter
    }

    private fun setListener() {
        binding.tvViewRecentSearch.setOnClickListener {
            getResultText.launch(Intent(this, RecentSearchKeywordActivity::class.java))
        }
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->  // SoftInput Text Done
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                callSearch(searchViewModel.inputSearchText.value)
            }
            false
        }
    }

    companion object {
        const val LINK_NAME = "link"
        const val SEARCH_KEYWORD = "searchKeyword"
    }
}