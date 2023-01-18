package com.flow.moviesearchflowhomework.data.service

import com.flow.moviesearchflowhomework.data.dto.NaverSearchQueryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface NaverService {
    @GET("v1/search/movie.json")
    suspend fun fetchSearchMovies(
        @Query("query") query: String,
        @Query("display") display: Int,
        @Query("start") start: Int
    ): Response<NaverSearchQueryDto>
}