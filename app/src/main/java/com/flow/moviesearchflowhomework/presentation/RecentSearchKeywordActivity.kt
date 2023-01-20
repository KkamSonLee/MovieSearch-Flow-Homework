package com.flow.moviesearchflowhomework.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.flow.moviesearchflowhomework.databinding.ActivityRecentSearchKeywordBinding
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.presentation.adapters.RecentSearchListAdapter
import com.flow.moviesearchflowhomework.presentation.util.*
import com.flow.moviesearchflowhomework.presentation.viewmodels.RecentSearchKeywordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentSearchKeywordActivity :
    BaseActivity<ActivityRecentSearchKeywordBinding>(
        ActivityRecentSearchKeywordBinding::inflate,
        TransitionMode.HORIZONTAL
    ) {
    private lateinit var recentSearchAdapter: RecentSearchListAdapter
    private val recentSearchKeywordViewModel by viewModels<RecentSearchKeywordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.recentViewModel = recentSearchKeywordViewModel
        setAdapter()
        setListener()
        collectData()
    }

    private fun collectData() {
        collectFlowWhenStarted(recentSearchKeywordViewModel.recentSearchList) { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    onSuccess(uiState)
                }
                is UiState.Loading -> {
                    onLoad()
                }
                is UiState.Error -> {}
                is UiState.Init -> {}
            }
        }
    }

    private fun setListener() {
        binding.recentAppbar.appbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun onSuccess(recentSearchItem: UiState.Success<List<RecentSearchKeywordEntity>>) {   //Progress Gone, List Submit
        binding.pbProgress.setGone()
        if (recentSearchItem.data.isEmpty()) {
            binding.tvEmpty.setVisible()
        } else {
            binding.tvEmpty.setGone()
        }
        recentSearchAdapter.submitList(recentSearchItem.data)
    }

    private fun onLoad() {  //Progress visible
        binding.pbProgress.setVisible()
    }

    private fun setAdapter() {
        recentSearchAdapter =
            RecentSearchListAdapter(
                recentKeywordClickListener = { keywordEntity ->     //Move to SearchActivity
                    backToSearch(keywordEntity.keyword)
                }, deleteKeywordClickListener = { keywordEntity ->  //delete 1 Item
                    deleteKeyword(keywordEntity)
                })
        binding.rvRecentList.adapter = recentSearchAdapter
    }

    private fun backToSearch(keyword: String) {         //Return to getResultText(Search Activity)
        val intent = Intent(this, MovieSearchActivity::class.java).apply {
            putExtra("searchKeyword", keyword)    //Search Keyword
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun deleteKeyword(keywordEntity: RecentSearchKeywordEntity) {  //remove call dao
        recentSearchKeywordViewModel.deleteRecentKeyword(keywordEntity)
    }
}