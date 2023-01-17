package com.flow.moviesearchflowhomework.presentation.util

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.flow.moviesearchflowhomework.R

abstract class BaseActivity<T : ViewDataBinding>(private val inflate: (LayoutInflater) -> T) :  // or Layout ID
    AppCompatActivity() {
    constructor(
        inflate: (LayoutInflater) -> T,
        transitionMode: TransitionMode = TransitionMode.NONE
    ) : this(inflate) {
        this.transitionMode = transitionMode
    }

    private lateinit var transitionMode: TransitionMode
    private val _binding: T? by lazy { inflate.invoke(layoutInflater) }
    protected val binding get() = _binding ?: throw  NullPointerException("Binding Is Null")


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(binding.root)
        when (transitionMode) {
            TransitionMode.HORIZONTAL -> overridePendingTransition(
                R.anim.horizontal_enter,
                R.anim.none
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