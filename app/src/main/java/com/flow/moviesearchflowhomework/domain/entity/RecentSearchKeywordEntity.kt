package com.flow.moviesearchflowhomework.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "recent_search")
data class RecentSearchKeywordEntity(
    @PrimaryKey
    val keyword: String,   // 동일 키워드로 검색시 업데이트 되기 위함
    val createAt: LocalDateTime
)