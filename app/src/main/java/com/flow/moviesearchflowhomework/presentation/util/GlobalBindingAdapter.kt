package com.flow.moviesearchflowhomework.presentation.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.flow.moviesearchflowhomework.R

@BindingAdapter("app:loadRemoteRoundedImage")
fun ImageView.loadRemoteRoundedImage(url: String) {
    if (url.isBlank()) {            // Image가 없는 것이 올 때는 placeholder 띄움
        load(R.drawable.placeholder) {
            transformations(RoundedCornersTransformation(12F))
        }
        return
    }
    load(url) {  // Use Coil
        crossfade(true)
        transformations(RoundedCornersTransformation(12F))
        placeholder(R.drawable.placeholder)
    }
}