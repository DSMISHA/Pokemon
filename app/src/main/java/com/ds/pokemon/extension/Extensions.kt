package com.ds.pokemon.extension

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.util.*


fun View.setEnabling(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.alpha = if (isEnabled) 1.0f else 0.3f
}

fun ImageView.loadImage(imgUrl: String, placeHolder: Drawable? = null, isCache: Boolean = false) =
    Glide.with(context)
        .load(imgUrl)
        .centerCrop()
        .placeholder(placeHolder)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
        .diskCacheStrategy(if (isCache) DiskCacheStrategy.ALL else DiskCacheStrategy.NONE)
        .into(this)

fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
