package com.flow.moviesearchflowhomework.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.domain.repository.HomeRepository
import com.flow.moviesearchflowhomework.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentSearchKeywordViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {

    private val _recentSearchList =
        MutableStateFlow<UiState<List<RecentSearchKeywordEntity>>>(UiState.Loading)
    val recentSearchList get() = _recentSearchList.asStateFlow()

    init {
        viewModelScope.launch {
            _recentSearchList.value = UiState.Success(homeRepository.fetchRecentSearch())
        }
    }

    fun deleteRecentKeyword(keywordEntity: RecentSearchKeywordEntity) {
        viewModelScope.launch {
            homeRepository.deleteRecentSearch(keywordEntity)
            _recentSearchList.value = UiState.Success(homeRepository.fetchRecentSearch())
        }
    }
}