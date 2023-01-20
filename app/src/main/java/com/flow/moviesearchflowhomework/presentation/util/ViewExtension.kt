package com.flow.moviesearchflowhomework.presentation.util
import android.view.View

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setOnSingleClickListener(onSingleClick: (View) -> Unit) {
    setOnClickListener(SingleClickListener { onSingleClick(it) })
}