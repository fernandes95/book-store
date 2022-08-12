package com.example.bookstore.ui.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.data.api.dto.VolumeDto

class VolumeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.volumeListItemTitleTv)
    private val thumb: ImageView = itemView.findViewById(R.id.volumeListItemThumbIv)

    fun bind(item: VolumeDto.Volume?, context: Context) {
        title.text = item?.volumeInfo?.title

        if(item?.volumeInfo?.imageLinks?.thumbnail.isNullOrEmpty()) return
        //TODO ADD ERROR DRAWABLE
        Glide.with(context)
            .load(item?.volumeInfo?.imageLinks?.thumbnail)
            .into(thumb)
    }
}