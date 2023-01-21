package com.flow.moviesearchflowhomework.presentation

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.flow.moviesearchflowhomework.databinding.ActivityMovieSearchBinding
import com.flow.moviesearchflowhomework.presentation.adapters.SearchListPagerAdapter
import com.flow.moviesearchflowhomework.presentation.util.BaseActivity
import com.flow.moviesearchflowhomework.presentation.util.collectFlowWhenStarted
import com.flow.moviesearchflowhomework.presentation.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieSearchActivity :
    BaseActivity<ActivityMovieSearchBinding>(ActivityMovieSearchBinding::inflate) {
    private lateinit var searchAdapter: SearchListPagerAdapter
    private val searchViewModel by viewModels<SearchViewModel>()
    private var job: Job? = null
    private val getResultText: ActivityResultLauncher<Intent> =    // Get Activity Result
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getStringExtra(SEARCH_KEYWORD).toString()
                searchViewModel.inputSearchText.value = data  //edit text
                callSearch(data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.searchViewModel = searchViewModel
        setAdapter()
        setListener()
    }

    private fun callSearch(keyword: String) {      // Progress, Collect Paging List
        job = lifecycleScope.launch {
            searchViewModel.initSearchCollect(keyword).flowWithLifecycle(lifecycle)
                .collectLatest { paging ->
                    searchAdapter.submitData(paging)
                }
        }
        collectFlowWhenStarted(searchAdapter.loadStateFlow) { state ->
            binding.pbProgress.isVisible = state.refresh is LoadState.Loading
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

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

    companion object {
        const val LINK_NAME = "link"
        const val SEARCH_KEYWORD = "searchKeyword"
    }
}