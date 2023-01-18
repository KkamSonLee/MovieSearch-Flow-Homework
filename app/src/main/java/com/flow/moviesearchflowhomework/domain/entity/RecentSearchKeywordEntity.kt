package com.flow.moviesearchflowhomework.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "recent_search")
data class RecentSearchKeywordEntity(
    @PrimaryKey
    val keyword: String,
    val createAt: LocalDateTime
)