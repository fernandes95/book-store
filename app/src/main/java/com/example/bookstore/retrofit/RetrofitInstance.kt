package com.example.bookstore.retrofit

import com.example.bookstore.interfaces.VolumeApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitInstance {

    object RetrofitClient {

        private const val BASE_URL = "https://www.googleapis.com/books/v1/"

        val retrofitClient: Retrofit.Builder by lazy {

            val okHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(RequestInterceptor)
                .build()

            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
        }

        val apiInterface: VolumeApi by lazy {
            retrofitClient
                .build()
                .create(VolumeApi::class.java)
        }
    }

    object RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            println("Outgoing request to ${request.url}")
            return chain.proceed(request)
        }
    }
}