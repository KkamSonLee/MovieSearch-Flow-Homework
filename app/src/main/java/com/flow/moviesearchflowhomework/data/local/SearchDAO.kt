package com.flow.moviesearchflowhomework.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flow.moviesearchflowhomework.domain.entity.RecentSearchKeywordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearch(data: RecentSearchKeywordEntity)

    @Update
    suspend fun updateRecentSearch(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity)

    @Delete
    suspend fun deleteRecentSearch(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity)

    @Query("select * from recent_search order by createAt desc ")
    suspend fun getAllRecentList(): List<RecentSearchKeywordEntity>
}