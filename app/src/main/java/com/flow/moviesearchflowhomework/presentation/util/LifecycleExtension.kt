package com.flow.moviesearchflowhomework.presentation.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

inline fun <T, R> R.collectFlowWhenStarted(
    flow: Flow<T>, crossinline block: suspend (T) -> Unit
) = collectFlow(flow, block) // Lifecycle.State.STARTED는 Default로 들어감

inline fun <T, R> R.collectFlow(
    flow: Flow<T>, crossinline block: suspend (T) -> Unit   // 비지역 반환 해결, inline func + 람다 사용시 cross-inline 사용
) {
    when (this) {
        is AppCompatActivity -> flow.flowWithLifecycle(lifecycle).onEach { block(it) }
            .launchIn(lifecycleScope)
        is Fragment -> flow.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach { block(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        else -> {}
    }
}