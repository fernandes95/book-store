package com.example.bookstore.services

import com.example.bookstore.dto.ErrorResponse
import com.example.bookstore.dto.Volume
import com.example.bookstore.interfaces.VolumeApi
import com.example.bookstore.retrofit.RetrofitInstance
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.ResponseBody
import retrofit2.Response

class VolumesService {

    private fun volumesResponse() : Response<List<Volume.Volume>>{
        val retrofit = RetrofitInstance.RetrofitClient.getClient()
        val volumeApi = retrofit.create(VolumeApi::class.java)

        return volumeApi.getVolumesQuery(
            mapOf("q" to "ios",
                "maxResults" to "20",
                "startIndex" to "0")
                ).execute()
    }

    fun successfulUsersResponse() {
        val volumesResponse = volumesResponse()

        val successful = volumesResponse.isSuccessful
        val httpStatusCode = volumesResponse.code()
        val httpStatusMessage = volumesResponse.message()

        val body: List<Volume.Volume>? = volumesResponse.body()
    }

    fun errorUsersResponse() {
        val errorBody: ResponseBody? = volumesResponse().errorBody()
        val mapper = ObjectMapper()
        val mappedBody: ErrorResponse? = errorBody?.let { notNullErrorBody ->
            mapper.readValue(notNullErrorBody.toString(), ErrorResponse::class.java)
        }
    }

    fun headersUsersResponse() {
        val headers = volumesResponse().headers()
        val customHeaderValue = headers["custom-header"]
    }
}