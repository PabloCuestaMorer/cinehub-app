package es.usj.pcuestam.cinehubapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.1.132:8080" // Home
    //private const val BASE_URL = "http://192.168.223.11:8080" // POCO F3
    //private const val BASE_URL = "http://172.18.64.130:8080" // USJ


    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}