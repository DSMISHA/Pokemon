package com.ds.pokemon.extension

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ds.pokemon.R
import com.google.android.material.snackbar.Snackbar
import java.util.*

/** enables/disables a view with transparency modifications */
fun View.setEnabling(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.alpha = if (isEnabled) 1.0f else 0.3f
}

/** loads an image to view from url */
fun ImageView.loadImage(
    imgUrl: String,
    placeHolder: Drawable? = null,
    isCache: Boolean = false,
    onLoadingFinished: (() -> Unit)? = null
) = Glide.with(context)
    .load(imgUrl)
    .centerCrop()
    .placeholder(placeHolder)
    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
    .diskCacheStrategy(if (isCache) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE)
    .listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished?.invoke()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished?.invoke()
            return false
        }
    }).into(this)

/** Capitalizes first letter in a string */
fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

/** Returns MutableLiveData object as immutable LiveData */
fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

/** Shows error snackbar for specified root view */
fun View.showErrorSnackBar() {
    Snackbar.make(this, resources.getString(R.string.error_loading_text), Snackbar.LENGTH_LONG)
        .setAction(resources.getString(R.string.error_loading_btn_title)) { }
        .setActionTextColor(
            ResourcesCompat.getColor(resources, android.R.color.holo_red_light, null)
        ).show()
}
