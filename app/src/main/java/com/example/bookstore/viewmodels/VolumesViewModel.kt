package com.example.bookstore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.repository.VolumesRepository

class VolumesViewModel : ViewModel() {

    fun getListData(): LiveData<List<VolumeDto.Volume>>? {
        return VolumesRepository.getVolumesApiCall()
    }
}