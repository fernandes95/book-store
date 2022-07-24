package com.example.bookstore.interfaces

import com.example.bookstore.dto.Volume
import retrofit2.Call
import retrofit2.http.GET

interface VolumeApi {

    @GET("volumes")
    fun getVolumes(): Call<List<Volume.Volume>>
}