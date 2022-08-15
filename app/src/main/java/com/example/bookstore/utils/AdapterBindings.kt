package com.example.bookstore.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.bookstore.R

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .error(R.drawable.no_photo)
        .into(imageView)
}