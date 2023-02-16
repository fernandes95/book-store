package com.example.bookstore.data.repositories

import com.example.bookstore.data.api.VolumeApi
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.di.DaggerAppComponent
import com.example.bookstore.utils.API_MAX_RESULTS
import javax.inject.Inject

class VolumesRepository {

    @Inject
    lateinit var volumeApi: VolumeApi

    init {
        DaggerAppComponent.create().inject(this)
    }

    private suspend fun getVolumes(query : String, startIndex : Int) : ArrayList<VolumeDto.Volume> {
        val volumes = volumeApi.getVolumesQuery(
            mapOf(
                "q" to query,
                "maxResults" to API_MAX_RESULTS,
                "startIndex" to startIndex.toString()
            )
        )

        return volumes.items ?: arrayListOf()
    }

    private suspend fun getVolume(volumeId : String) : VolumeDto.Volume =
        volumeApi.getVolumeById(volumeId)

    suspend fun fetchVolumesFromApi(query : String = "android", startIndex : Int = 0) = getVolumes(query, startIndex)
    suspend fun fetchVolumeFromApi(volumeId : String) = getVolume(volumeId)
}