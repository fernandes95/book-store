package com.example.bookstore.viewmodels

import androidx.lifecycle.*
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.repository.VolumesRepository
import com.example.bookstore.room.FavoritesRepository
import com.example.bookstore.room.VolumeEntity
import com.example.bookstore.room.toVolumeEntity
import kotlinx.coroutines.launch

class VolumeDetailViewModel(private var repository: FavoritesRepository) : ViewModel() {

    var volumeId : String? = null
    private var selectedVolume : VolumeDto.Volume? = null

    val favorites: LiveData<List<VolumeEntity>> = repository.allVolumes.asLiveData()
    val isFavorite: Boolean = repository.volumeExists(volumeId!!)

    fun insert() = viewModelScope.launch {
        selectedVolume?.toVolumeEntity()?.let { repository.insert(it) }
    }

    fun delete() = viewModelScope.launch {
        selectedVolume?.toVolumeEntity()?.let { repository.delete(it) }
    }

    fun getVolume(volumeId : String): LiveData<VolumeDto.Volume>? {
        this.volumeId = volumeId
        val vol =  VolumesRepository.getVolumeDetailApiCall(volumeId)
        selectedVolume = vol.value
        return vol
    }
}

class VolumeDetailViewModelFactory(private val repository: FavoritesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VolumeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VolumeDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}