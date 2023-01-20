package com.flow.moviesearchflowhomework.presentation.util

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.flow.moviesearchflowhomework.R

abstract class BaseActivity<T : ViewDataBinding>(private val inflate: (LayoutInflater) -> T):  // or Layout ID
    AppCompatActivity() {
    constructor(                                    //기본 생성자 파라미터, 하나는 default value를 넣어도 선택적으로 파라미터를 보낼 수가 없음 (@AndroidEntryPoint 때문일 가능성이 높지만 현재는 해당 방법으로 메소드 오버로딩 사용)
        inflate: (LayoutInflater) -> T,
        transitionMode: TransitionMode              // 선택적 animation 적용
    ) : this(inflate) {
        this.transitionMode = transitionMode
    }

    private var transitionMode: TransitionMode = TransitionMode.NONE
    private val _binding: T? by lazy { inflate.invoke(layoutInflater) }
    protected val binding
        get() = _binding
            ?: throw  NullPointerException("Binding Is Null")  //상속 받고 있는 Activity에서만 사용, (activity as XXXActivity) 로는 접근 불가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        when (transitionMode) {
            TransitionMode.HORIZONTAL -> overridePendingTransition(
                R.anim.horizontal_enter,  //해당 액티비티를 상속하고 현재 들어오는 액티비티
                R.anim.none   //이전 액티비티
            )
            else -> Unit
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            when (transitionMode) {
                TransitionMode.HORIZONTAL -> overridePendingTransition(
                    R.anim.none,
                    R.anim.horizontal_exit
                )
                else -> Unit
            }
        }
    }

    override fun finish() {
        super.finish()
        when (transitionMode) {
            TransitionMode.HORIZONTAL -> overridePendingTransition(
                R.anim.none,
                R.anim.horizontal_exit
            )
            else -> Unit
        }
    }
}