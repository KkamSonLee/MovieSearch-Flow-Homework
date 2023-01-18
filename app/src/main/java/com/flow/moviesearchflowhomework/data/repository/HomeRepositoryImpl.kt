package com.flow.moviesearchflowhomework.data.repository

import com.flow.moviesearchflowhomework.data.datasource.LocalRecentSearchDataSource
import com.flow.moviesearchflowhomework.data.datasource.RemoteSearchDataSource
import com.flow.moviesearchflowhomework.data.mapper.SearchItemMapper
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.domain.entity.SearchItem
import com.flow.moviesearchflowhomework.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteSearchDatasource: RemoteSearchDataSource,
    private val localRecentSearchDataSource: LocalRecentSearchDataSource,
    private val searchItemMapper: SearchItemMapper
) : HomeRepository {
    override suspend fun fetchMovie(keyword: String, display: Int, start: Int): List<SearchItem>? =
        remoteSearchDatasource.fetchSearchMovie(keyword, display, start)?.let {
            searchItemMapper.map(it)
        }

    override suspend fun fetchRecentSearch(): List<RecentSearchKeywordEntity> =
        localRecentSearchDataSource.getAllRecentKeyword()

    override suspend fun insertRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity) {
        localRecentSearchDataSource.insertRecentKeyword(recentSearchKeywordEntity)
        val currentList = localRecentSearchDataSource.getAllRecentKeyword()
        if (currentList.size > 10) {
            localRecentSearchDataSource.deleteRecentSearch(currentList[10])
        }
    }
}