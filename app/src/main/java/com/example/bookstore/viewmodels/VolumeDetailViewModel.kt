package com.example.bookstore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.repository.VolumesRepository

class VolumeDetailViewModel : ViewModel() {

    fun getVolume(volumeId: String): LiveData<VolumeDto.Volume>? {
        return VolumesRepository.getVolumeDetailApiCall(volumeId)
    }
}