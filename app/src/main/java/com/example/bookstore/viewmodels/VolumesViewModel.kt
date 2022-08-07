package com.example.bookstore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.repository.VolumesRepository
import com.example.bookstore.room.FavoriteDatabase
import com.example.bookstore.room.FavoritesRepository
import com.example.bookstore.room.VolumeEntity

class VolumesViewModel(repository: FavoritesRepository) : ViewModel() {

    val favorites: LiveData<List<VolumeEntity>> = repository.allVolumes.asLiveData()

    fun getListData(): LiveData<List<VolumeDto.Volume>>? {
        return VolumesRepository.getVolumesApiCall()
    }
}

class VolumesViewModelFactory(private val repository: FavoritesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VolumesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VolumesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}