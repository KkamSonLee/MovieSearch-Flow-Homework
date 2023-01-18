package com.flow.moviesearchflowhomework.presentation.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.flow.moviesearchflowhomework.R


@BindingAdapter("app:loadRemoteRoundedImage")
fun ImageView.loadRemoteRoundedImage(url: String?) {
    if (url == null) return
    load(url) {
        crossfade(true)
        transformations(RoundedCornersTransformation(12F))
        placeholder(R.drawable.placeholder)
    }
}