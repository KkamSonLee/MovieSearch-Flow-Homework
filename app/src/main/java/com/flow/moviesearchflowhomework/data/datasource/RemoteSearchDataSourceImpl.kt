package com.flow.moviesearchflowhomework.data.datasource

import com.flow.moviesearchflowhomework.data.dto.NaverSearchQueryDto
import com.flow.moviesearchflowhomework.data.service.NaverService
import javax.inject.Inject

class RemoteSearchDataSourceImpl @Inject constructor(private val naverService: NaverService) :
    RemoteSearchDataSource {
    override suspend fun fetchSearchMovie(
        keyword: String,
        display: Int,
        start: Int
    ): NaverSearchQueryDto? = naverService.fetchSearchMovies(keyword, display, start).body()
}