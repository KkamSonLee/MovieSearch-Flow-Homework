package com.flow.moviesearchflowhomework.data.datasource

import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity

interface LocalRecentSearchDataSource {
    suspend fun insertRecentKeyword(recentSearchKeywordEntity: RecentSearchKeywordEntity)
    suspend fun getAllRecentKeyword(): List<RecentSearchKeywordEntity>
    suspend fun updateRecentKeyword(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity)
    suspend fun deleteRecentSearch(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity)
}