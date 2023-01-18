package com.flow.moviesearchflowhomework.domain.repository

import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.domain.entity.SearchItem

interface HomeRepository {
    suspend fun fetchMovie(keyword: String, display: Int, start: Int): List<SearchItem>?
    suspend fun fetchRecentSearch(): List<RecentSearchKeywordEntity>
    suspend fun insertRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity)
}