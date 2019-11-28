package com.example.holapp.IO.response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiAdaper {

    private var API_SERVICE: ApiService? = null

    fun getApiService(): ApiService? {
        val baseUrl = "http://192.168.1.68:3035/"
//        val baseUrl = "http://192.168.43.189:3035/"

        if (API_SERVICE == null) {
            val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            API_SERVICE = retrofit.create(ApiService::class.java)
        }

        return API_SERVICE
    }
}