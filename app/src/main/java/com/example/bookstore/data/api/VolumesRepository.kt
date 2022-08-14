package com.example.bookstore.data.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookstore.data.api.dto.VolumeDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VolumesRepository {

    fun getVolumesApiCall(startIndex : Int = 0): MutableLiveData<ArrayList<VolumeDto.Volume>> {
        val volumesList = MutableLiveData<ArrayList<VolumeDto.Volume>>()
        val call = ApiService.getClient().getVolumesQuery(
            mapOf("q" to "ios",
                "maxResults" to "40",
                "startIndex" to startIndex.toString()))

        call.enqueue(object: Callback<VolumeDto.Volumes> {
            override fun onFailure(call: Call<VolumeDto.Volumes>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<VolumeDto.Volumes>,
                response: Response<VolumeDto.Volumes>
            ) {
                Log.v("DEBUG : ", response.body().toString())

                val list = response.body()?.items
                volumesList.value = list?.let { ArrayList(it) }
            }
        })

        return volumesList
    }

    fun getVolumeDetailApiCall(volumeId: String): MutableLiveData<VolumeDto.Volume> {
        val volume = MutableLiveData<VolumeDto.Volume>()
        val call = ApiService.getClient().getVolumeById(volumeId)

        call.enqueue(object: Callback<VolumeDto.Volume> {
            override fun onFailure(call: Call<VolumeDto.Volume>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
            }

            override fun onResponse(
                call: Call<VolumeDto.Volume>,
                response: Response<VolumeDto.Volume>
            ) {
                Log.v("DEBUG : ", response.body().toString())
                volume.value = response.body()
            }
        })

        return volume
    }
}