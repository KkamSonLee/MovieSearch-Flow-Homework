package com.flow.moviesearchflowhomework.data.dto

data class NaverSearchQueryDto(
    val display: Int,
    val items: List<Item>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
)