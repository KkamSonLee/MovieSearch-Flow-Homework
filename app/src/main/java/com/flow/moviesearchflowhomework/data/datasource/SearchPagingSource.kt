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
        val response =
            catchingApiCall {
                naverService.fetchSearchMovies(
                    query = query,
                    display = params.loadSize,
                    start = 1 + (key - 1) * 30           //1, 31, 61, 91 ... 순서로 호출하기 위함 start value
                )
            }?.let { dto ->
                if (dto.total < 1 + (key - 1) * 30) {  //start index가 total count을 넘어도 total 범위까지 display 배수를 빼주어 계속 불러와지므로 처리
                    listOf()
                } else {
                    mapper.map(dto)
                }
            } ?: listOf()    // 해당 Case가 catchingApiCall에서 잡힌 Exception, 현재는 따로 처리할 것이 없으므로 빈 배열 반환

        return try {
            LoadResult.Page(
                data = response,
                nextKey = if (response.isEmpty()) null else key + 1,
                prevKey = if (key == NAVER_STARTING_PAGE_INDEX) null else key, // Only paging forward.
            )
        } catch (e: IllegalArgumentException) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val NAVER_STARTING_PAGE_INDEX = 1
    }
}