package com.flow.moviesearchflowhomework.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.flow.moviesearchflowhomework.data.datasource.LocalRecentSearchDataSource
import com.flow.moviesearchflowhomework.data.datasource.SearchPagingSource
import com.flow.moviesearchflowhomework.data.mapper.SearchItemMapper
import com.flow.moviesearchflowhomework.data.service.NaverService
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import com.flow.moviesearchflowhomework.domain.entity.SearchItem
import com.flow.moviesearchflowhomework.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val naverService: NaverService,
    private val localRecentSearchDataSource: LocalRecentSearchDataSource,
    private val searchItemMapper: SearchItemMapper
) : HomeRepository {
    override fun fetchMovie(
        keyword: String
    ): Flow<PagingData<SearchItem>> =
        Pager(
            config = PagingConfig(
                pageSize = 30
            ), pagingSourceFactory = { SearchPagingSource(naverService, keyword, searchItemMapper) }
        ).flow

    override suspend fun fetchRecentSearch(): List<RecentSearchKeywordEntity> =
        localRecentSearchDataSource.getAllRecentKeyword()

    override suspend fun insertRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity) {
        localRecentSearchDataSource.insertRecentKeyword(recentSearchKeywordEntity)
        val currentList = localRecentSearchDataSource.getAllRecentKeyword()
        if (currentList.size > 10) {
            localRecentSearchDataSource.deleteRecentSearch(currentList[10])   // LRU 기법처럼 가장 뒤에 있는 데이터부터 제거
        }
    }

    override suspend fun deleteRecentSearch(recentSearchKeywordEntity: RecentSearchKeywordEntity) {
        localRecentSearchDataSource.deleteRecentSearch(recentSearchKeywordEntity)
    }
}