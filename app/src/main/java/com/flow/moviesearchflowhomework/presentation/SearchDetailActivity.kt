package com.flow.moviesearchflowhomework.presentation

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.flow.moviesearchflowhomework.databinding.ActivitySearchDetailBinding
import com.flow.moviesearchflowhomework.presentation.util.BaseActivity
import com.flow.moviesearchflowhomework.presentation.util.TransitionMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDetailActivity :
    BaseActivity<ActivitySearchDetailBinding>(
        inflate = ActivitySearchDetailBinding::inflate,
        transitionMode = TransitionMode.HORIZONTAL
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListener()

        with(binding.webview) {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient() // 안정성을 위해서 크로미움 클라이언트도 적용
            with(settings) {
                loadsImagesAutomatically = true // 이미지 자동 로드
                javaScriptEnabled = true
            }
            val link = intent.getStringExtra("link")
            clearCache(true)
            link?.let { loadUrl(it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding.webview) {
            clearHistory()
            clearCache(true)
            loadUrl("about:blank")
            destroy()
        }
    }

    private fun setListener() {
        binding.include.appbar.setNavigationOnClickListener {
            finish()
        }
    }
}