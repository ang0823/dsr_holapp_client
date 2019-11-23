package com.example.holapp.IO.response

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiAdaper {

    private var API_SERVICE: ApiService? = null

    fun getApiService(): ApiService? {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logger)

        // A partir de aquí se comienza el verdadero uso de retrofit
        //val baseUrl = "http://192.168.1.68:3035/"
        val baseUrl = "http://192.168.43.189:3035/"

        if (API_SERVICE == null) {
            val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
                .build()
            API_SERVICE = retrofit.create(ApiService::class.java)
        }


        return API_SERVICE
    }
}