package com.flow.moviesearchflowhomework.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.domain.entity.SearchItem
import com.flow.moviesearchflowhomework.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModel() {
    val inputSearchText = MutableStateFlow<String>("")

    fun initSearchCollect(keyword: String): Flow<PagingData<SearchItem>> {
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
        return homeRepository.fetchMovie(keyword = keyword).cachedIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("VIEWMODEL", "onCleared")
    }

}