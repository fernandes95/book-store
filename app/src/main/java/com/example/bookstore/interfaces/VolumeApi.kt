package com.example.bookstore.interfaces

import com.example.bookstore.dto.VolumeDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface VolumeApi {

    @GET("volumes")
    fun getVolumes(): Call<List<VolumeDto.Volume>>

    @GET("volumes")
    fun getVolumesQuery(@QueryMap parameters: Map<String, String>): Call<VolumeDto.Volumes>
}