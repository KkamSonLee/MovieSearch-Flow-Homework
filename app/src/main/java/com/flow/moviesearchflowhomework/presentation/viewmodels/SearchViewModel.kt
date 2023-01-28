package com.flow.moviesearchflowhomework.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {
    val inputSearchText = MutableStateFlow<String>("")

    private val resultKeyword = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class)
    val searchKeyword =
        resultKeyword.flatMapLatest { homeRepository.fetchMovie(it) }.cachedIn(viewModelScope)
    fun addSearchKeyword(keyword: String) {
        if (keyword.isNotBlank()) {
            viewModelScope.launch {
                homeRepository.insertRecentSearch(
                    RecentSearchKeywordEntity(
                        keyword,
                        LocalDateTime.now()
                    )
                )
            }
        }
        resultKeyword.value = keyword
    }
}