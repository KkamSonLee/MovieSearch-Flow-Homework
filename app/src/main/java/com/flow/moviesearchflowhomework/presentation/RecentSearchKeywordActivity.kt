package com.flow.moviesearchflowhomework.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flow.moviesearchflowhomework.databinding.ActivityRecentSearchKeywordBinding
import com.flow.moviesearchflowhomework.presentation.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentSearchKeywordActivity :
    BaseActivity<ActivityRecentSearchKeywordBinding>(ActivityRecentSearchKeywordBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvTest.setOnClickListener {
            val intent = Intent(this, MovieSearchActivity::class.java).apply {
                putExtra("searchKeyword", "Hi")
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.recentAppbar.appbar.setNavigationOnClickListener {
            finish()
        }
    }
}