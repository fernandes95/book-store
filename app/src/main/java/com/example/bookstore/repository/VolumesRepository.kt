package com.example.bookstore.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object VolumesRepository {
    private val volumesList = MutableLiveData<List<VolumeDto.Volume>>()

    fun getServicesApiCall(): MutableLiveData<List<VolumeDto.Volume>> {

        val call = RetrofitInstance.RetrofitClient.apiInterface.getVolumesQuery(
                        mapOf("q" to "ios",
                        "maxResults" to "20",
                        "startIndex" to "0"))

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
                volumesList.value = list
            }
        })

        return volumesList
    }
}