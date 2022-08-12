package com.example.bookstore.data.api

import com.example.bookstore.data.api.dto.VolumeDto
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface VolumeApi {
    @GET("volumes")
    fun getVolumesQuery(@QueryMap parameters: Map<String, String>): Call<VolumeDto.Volumes>

    @GET("volumes")
    fun getVolumesObservableQuery(@QueryMap parameters: Map<String, String>): Observable<VolumeDto.Volumes>

    @GET("volumes/{volumeId}")
    fun getVolumeById(@Path("volumeId") volumeId: String): Call<VolumeDto.Volume>
}