package com.flow.moviesearchflowhomework.data.datasource

import com.flow.moviesearchflowhomework.data.dto.NaverSearchQueryDto

interface RemoteSearchDataSource {
    suspend fun fetchSearchMovie(
        keyword: String,
        display: Int,
        start: Int
    ): NaverSearchQueryDto?
}