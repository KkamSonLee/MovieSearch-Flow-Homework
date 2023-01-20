package com.flow.moviesearchflowhomework.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flow.moviesearchflowhomework.data.mapper.SearchItemMapper
import com.flow.moviesearchflowhomework.data.service.NaverService
import com.flow.moviesearchflowhomework.data.util.catchingApiCall
import com.flow.moviesearchflowhomework.domain.entity.SearchItem

class SearchPagingSource(
    private val naverService: NaverService,
    private val query: String,
    private val mapper: SearchItemMapper
) :
    PagingSource<Int, SearchItem>() {

    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        val key = params.key ?: NAVER_STARTING_PAGE_INDEX

        var total = Int.MAX_VALUE
        val response =
            catchingApiCall {
                naverService.fetchSearchMovies(
                    query = query,
                    display = params.loadSize,
                    start = 1 + (key - 1) * params.loadSize         //1, 31, 61, 91 ... 순서로 호출하기 위함 start value
                )
            }?.let { dto ->
                total = dto.total
                if (dto.total < 1 + (key - 1) * params.loadSize) {  //start index가 total count을 넘어도 total 범위까지 display 배수를 빼주어 계속 불러와지므로 처리
                    listOf()
                } else {
                    mapper.map(dto)
                }
            } ?: listOf()    // 해당 Case가 catchingApiCall에서 잡힌 Exception, 현재는 따로 처리할 것이 없으므로 빈 배열 반환
        return try {
            LoadResult.Page(
                data = response,
                nextKey = if (response.isEmpty() || total < 1 + key * params.loadSize) null else key + (params.loadSize / 30),  //초기에 loadSize * 3으로 호출 되기 때문에 key를 그만큼 업데이트 해주었음
                prevKey = if (key == NAVER_STARTING_PAGE_INDEX) null else key, // Only paging forward.
            )
        } catch (e: IllegalArgumentException) {   //LoadResult.Page 객체 생성시에 require 부분에서 해당 exception throw 하기 때문에 처리
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val NAVER_STARTING_PAGE_INDEX = 1
    }
}