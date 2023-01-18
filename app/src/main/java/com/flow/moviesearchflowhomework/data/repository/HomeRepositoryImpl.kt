package com.flow.moviesearchflowhomework.data.repository

import com.flow.moviesearchflowhomework.data.datasource.RemoteSearchDataSource
import com.flow.moviesearchflowhomework.data.mapper.SearchItemMapper
import com.flow.moviesearchflowhomework.domain.entity.SearchItem
import com.flow.moviesearchflowhomework.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteSearchDatasource: RemoteSearchDataSource,
    private val searchItemMapper: SearchItemMapper
) : HomeRepository {
    override suspend fun fetchMovie(keyword: String, display: Int, start: Int): List<SearchItem>? =
        remoteSearchDatasource.fetchSearchMovie(keyword, display, start)?.let {
            searchItemMapper.map(it)
        }
}
