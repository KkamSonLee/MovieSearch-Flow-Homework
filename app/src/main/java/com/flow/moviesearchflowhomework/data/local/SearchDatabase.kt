package com.flow.moviesearchflowhomework.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity

@Database(entities = [RecentSearchKeywordEntity::class], version = 1)
@TypeConverters(DateTypeConverter::class)   //LocalDateTime Convert 용
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDAO(): SearchDAO
}