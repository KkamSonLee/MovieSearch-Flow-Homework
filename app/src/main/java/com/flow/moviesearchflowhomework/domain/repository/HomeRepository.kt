package com.flow.moviesearchflowhomework.domain.repository

import androidx.paging.PagingData
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.domain.entity.SearchItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun fetchMovie(keyword: String): Flow<PagingData<SearchItem>>
    suspend fun fetchRecentSearch(): List<RecentSearchKeywordEntity>
    suspend fun insertRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity)
    suspend fun deleteRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity)
}