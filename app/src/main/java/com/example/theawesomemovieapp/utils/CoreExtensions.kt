package com.example.theawesomemovieapp.utils

import android.content.res.Resources
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("srcUrl")
fun ImageView.load(posterPath: String?) {
    if (!posterPath.isNullOrEmpty()) {
        // listener que acessa width do imageView quando o layout é construído
        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // remove o listener após a primeira chamada
                viewTreeObserver.removeOnGlobalLayoutListener(this)

                Glide.with(this@load)
                    .load(ImageBuilder.build(posterPath, width))
                    .into(this@load)
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)
    } else {
        this.setImageDrawable(null)
    }
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()