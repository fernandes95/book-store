package com.example.bookstore.services

import com.example.bookstore.interfaces.VolumeApi
import com.example.bookstore.retrofit.RetrofitInstance

class VolumesService {
    private val retrofit = RetrofitInstance.RetrofitClient.getClient()
    private val volumeApi = retrofit.create(VolumeApi::class.java)
}