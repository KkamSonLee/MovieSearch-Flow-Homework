package com.flow.moviesearchflowhomework.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.domain.entity.SearchItem
import com.flow.moviesearchflowhomework.domain.repository.HomeRepository
import com.flow.moviesearchflowhomework.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {
    private val _searchData = MutableStateFlow<UiState<List<SearchItem>>>(UiState.Init)
    val searchData get() = _searchData.asStateFlow()
    val inputSearchText = MutableStateFlow<String>("")

    fun callSearch(keyword: String) {
        _searchData.value = UiState.Loading
        viewModelScope.launch {
            _searchData.value =
                UiState.Success(homeRepository.fetchMovie(keyword = keyword, 10, 1) ?: listOf())
            homeRepository.insertRecentSearch(
                RecentSearchKeywordEntity(
                    keyword,
                    LocalDateTime.now()
                )
            )
        }
    }
}