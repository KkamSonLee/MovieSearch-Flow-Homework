package com.flow.moviesearchflowhomework.data.datasource

import com.flow.moviesearchflowhomework.data.local.SearchDAO
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import javax.inject.Inject

class LocalRecentSearchDataSourceImpl @Inject constructor(private val dao: SearchDAO) :
    LocalRecentSearchDataSource {
    override suspend fun insertRecentKeyword(recentSearchKeywordEntity: RecentSearchKeywordEntity) {
        dao.insertRecentSearch(recentSearchKeywordEntity)
    }

    override suspend fun getAllRecentKeyword(): List<RecentSearchKeywordEntity> =
        dao.getAllRecentList()

    override suspend fun updateRecentKeyword(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity) {
        dao.updateRecentSearch(*recentSearchKeywordEntity)
    }

    override suspend fun deleteRecentSearch(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity) {
        dao.deleteRecentSearch(*recentSearchKeywordEntity)
    }

}