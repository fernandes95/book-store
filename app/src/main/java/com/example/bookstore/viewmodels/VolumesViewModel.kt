package com.example.bookstore.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.repository.VolumesRepository
import com.example.bookstore.room.FavoritesRepository
import com.example.bookstore.room.VolumeEntity

class VolumesViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = FavoritesRepository(app)
    var favorites : LiveData<List<VolumeEntity>>? = null

    init {
        favorites = repository.getAll()
    }

    fun getFavoriteUid(id: String): Boolean? {
        val list = favorites?.value?.toList() ?: return false

        return list?.first{item -> item.id == id} != null
    }

    fun getListData(): LiveData<List<VolumeDto.Volume>>? {
        return VolumesRepository.getVolumesApiCall()
    }
}