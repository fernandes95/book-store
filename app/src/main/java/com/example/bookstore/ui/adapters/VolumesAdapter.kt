package com.example.bookstore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.R
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.ui.viewholders.VolumeViewHolder

class VolumesAdapter (private val context: Context, private val items : LiveData<List<VolumeDto.Volume>>, private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<VolumeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolumeViewHolder {
        return VolumeViewHolder(LayoutInflater.from(context).inflate(R.layout.volume_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.value!!.size
    }

    override fun onBindViewHolder(holder: VolumeViewHolder, position: Int) {
        val item = items.value!![position]

        holder.bind(item, context)
        holder.itemView.setOnClickListener { onClickListener.onClick(item.id)}
    }

    class OnClickListener(val clickListener: (volumeId: String) -> Unit) {
        fun onClick(volumeId: String) = clickListener(volumeId)
    }
}
