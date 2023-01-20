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

    @Insert(onConflict = OnConflictStrategy.REPLACE) //LRU -> 넣으려는 데이터가 이미 있는 데이터면 가장 최신으로 업데이트
    suspend fun insertRecentSearch(data: RecentSearchKeywordEntity)

    @Update
    suspend fun updateRecentSearch(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity)  //여러개를 넣을 수 있게 해야함, List로 받아서 처리하는 것보다 vararg로 처리하는게 훨씬 빠름

    @Delete
    suspend fun deleteRecentSearch(vararg recentSearchKeywordEntity: RecentSearchKeywordEntity)

    @Query("select * from recent_search order by createAt desc ")   //최신순으로 뽑아줌
    suspend fun getAllRecentList(): List<RecentSearchKeywordEntity>
}