package com.example.bookstore.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.R
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.databinding.VolumeListItemBinding

class VolumesAdapter (private val items : ArrayList<VolumeDto.Volume>, private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<VolumeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolumeViewHolder {
        return VolumeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.volume_list_item, parent,   false))
    }

    override fun getItemCount(): Int { return items.size }

    override fun onBindViewHolder(holder: VolumeViewHolder, position: Int) {
        val item = items[position]
        holder.itemBinding.volume = item
        holder.itemView.setOnClickListener { onClickListener.onClick(item.id)}
    }

    class OnClickListener(val clickListener: (volumeId: String) -> Unit) {
        fun onClick(volumeId: String) = clickListener(volumeId)
    }

    fun setUpData(volumes: List<VolumeDto.Volume>) {
        with(items){
            clear()
            addAll(volumes)
            notifyDataSetChanged()
        }
    }
}

class VolumeViewHolder(val itemBinding: VolumeListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
