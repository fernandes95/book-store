package com.example.bookstore.interfaces

import com.example.bookstore.dto.Volume
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface VolumeApi {

    @GET("volumes")
    fun getVolumes(): Call<List<Volume.Volume>>

    @GET("volumes")
    fun getVolumesQuery(@QueryMap parameters: Map<String, String>): Call<List<Volume.Volume>>
}