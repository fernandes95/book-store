package com.example.bookstore.services

class VolumesService {
    /*private val retrofit = RetrofitInstance.RetrofitClient.getClient()
    private val volumeApi = retrofit.create(VolumeApi::class.java)

    fun getVolumes() : Response<List<Volume.Volume>>{
        return volumeApi.getVolumesQuery(
            mapOf("q" to "ios",
                "maxResults" to "20",
                "startIndex" to "0")
                ).execute()
    }*/

    /*fun successfulUsersResponse() {
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
    }*/
}