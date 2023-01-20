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
        MutableStateFlow<UiState<List<RecentSearchKeywordEntity>>>(UiState.Loading)  //시작과 동시에 로드하기 떄문에(init block) 로드를 초기값으로 setting
    val recentSearchList get() = _recentSearchList.asStateFlow()   //Immutable StateFlow

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