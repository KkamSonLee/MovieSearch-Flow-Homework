package com.flow.moviesearchflowhomework.data.service

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface NaverService {
    @GET("v1/search/movie.json")
    fun fetchSearchMovies(/*
        @Header("X-Naver-Client-Id") clientId: String = "4X7Z074B7gOxj0qI58lo", AuthInterceptor 사용
        @Header("X-Naver-Client-Secret") clientSecret: String = "q9QIDTlWlG",*/
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null
    ) {

    }
}