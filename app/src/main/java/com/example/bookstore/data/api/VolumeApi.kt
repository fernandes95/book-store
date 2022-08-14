package com.example.bookstore.data.api

import com.example.bookstore.data.models.dto.VolumeDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface VolumeApi {
    @GET("volumes")
    fun getVolumesQuery(@QueryMap parameters: Map<String, String>): Call<VolumeDto.Volumes>

    @GET("volumes/{volumeId}")
    fun getVolumeById(@Path("volumeId") volumeId: String): Call<VolumeDto.Volume>
}