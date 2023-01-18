package com.flow.moviesearchflowhomework.presentation

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
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

        clickListener()

        with(binding.webview) {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient() // 안정성을 위해서 크로미움 클라이언트도 적용
            with(settings) {
                loadsImagesAutomatically = true // 이미지 자동 로드
                javaScriptEnabled = true
                // 웹페이지 내부가 javaScript를 통한
                //동작 동적이 있음 따라서 ture로 설정
                allowContentAccess = true
                // content URI 사용을 위함
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                // 쿠팡링크등 Dynamic link 허용을 위함
                cacheMode = WebSettings.LOAD_DEFAULT
            }
            val link = intent.getStringExtra("link")
            link?.let { loadUrl(it) }
        }
    }

    private fun clickListener() {
        binding.include.appbar.setNavigationOnClickListener {
            finish()
        }
    }

}