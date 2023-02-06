package com.example.bookstore.data.api

import com.example.bookstore.data.models.dto.VolumeDto
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface VolumeApi {
    @GET("volumes")
    suspend fun getVolumesQuery(@QueryMap parameters: Map<String, String>): VolumeDto.Volumes

    @GET("volumes/{volumeId}")
    suspend fun getVolumeById(@Path("volumeId") volumeId: String): VolumeDto.Volume
}