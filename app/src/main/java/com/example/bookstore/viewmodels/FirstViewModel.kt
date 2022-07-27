package com.example.bookstore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.repository.VolumesRepository

class FirstViewModel : ViewModel() {

    var liveDataList: MutableLiveData<List<VolumeDto.Volume>>? = null

    fun getListData(): LiveData<List<VolumeDto.Volume>>? {
        return VolumesRepository.getServicesApiCall()
    }
}