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
    private val queryFlow: MutableSharedFlow<String> =
        MutableSharedFlow(1) //default 0이면 초기 데이터 안 불러와짐

    fun onQueryChanged(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                queryFlow.emit(query)
            }
            Log.e("onQueryChanged", "Inner onQueryChanged $query")
        }
    }

    fun initSearchCollect(): Flow<PagingData<SearchItem>> {

        Log.e("onQueryChanged", "initSearchCollect()")
        val querySearchResults = queryFlow.asSharedFlow().flatMapLatest { query ->
            Log.e("asSharedFlow()", "flatMapLatest()")
            if (query.isNotBlank()) {
                homeRepository.insertRecentSearch(
                    RecentSearchKeywordEntity(
                        query,
                        LocalDateTime.now()
                    )
                )
            }
            homeRepository.fetchMovie(keyword = query).cachedIn(viewModelScope)
        }
        return querySearchResults
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("VIEWMODEL", "onCleared")
    }
}