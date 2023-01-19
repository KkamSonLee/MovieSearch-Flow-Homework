package com.flow.moviesearchflowhomework.presentation.util

import android.os.SystemClock
import android.view.View


class SingleClickListener(private val interval: Int = 2000, val onSingleClick: (View) -> Unit) :
    View.OnClickListener {
    private var lastClickTime: Long = 0
    override fun onClick(view: View) {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        if ((elapsedRealtime - lastClickTime) < interval) {
            return
        }
        lastClickTime = elapsedRealtime
        onSingleClick(view)
    }
}