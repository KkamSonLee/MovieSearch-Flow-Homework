package com.flow.moviesearchflowhomework.presentation

import android.os.Bundle
import com.flow.moviesearchflowhomework.databinding.ActivityMovieSearchBinding
import com.flow.moviesearchflowhomework.domain.entity.SearchItem
import com.flow.moviesearchflowhomework.presentation.adapters.RemoteSearchListAdapter
import com.flow.moviesearchflowhomework.presentation.util.BaseActivity

class MovieSearchActivity :
    BaseActivity<ActivityMovieSearchBinding>(ActivityMovieSearchBinding::inflate) {

    private lateinit var searchAdapter: RemoteSearchListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAdapter()
        applyListener()
    }

    private fun setAdapter() {
        searchAdapter = RemoteSearchListAdapter { }
        binding.rvSearchList.adapter = searchAdapter
        searchAdapter.submitList(
            listOf(
                SearchItem(
                    "아이(i)",
                    "",
                    "김현탁",
                    "https://movie-phinf.pstatic.net/20210210_291/1612940296044U0PKH_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=194463",
                    "2021",
                    "",
                    "9.13"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                ),
                SearchItem(
                    "아이(Eye)",
                    "",
                    "이강산",
                    "https://movie-phinf.pstatic.net/20200908_276/1599542167148wgKpM_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=197193",
                    "2021",
                    "",
                    "9.22"
                ),
                SearchItem(
                    "아이(Kid)",
                    "",
                    "이성경",
                    "https://movie-phinf.pstatic.net/20210714_41/1626226676307oB3Sp_JPEG/movie_image.jpg?type=f67",
                    "/movie/bi/mi/basic.naver?code=207009",
                    "2021",
                    "",
                    "9.26"
                )
            )
        )
    }

    private fun applyListener() {
        binding.tvViewRecentSearch.setOnClickListener {


        }

    }
}