package com.example.holapp.IO.response


import SignUpResponse
import com.example.holapp.models.Cliente
import retrofit2.Call
import retrofit2.http.*

/**
 * Aquí se definen todas las rutas que consumiremos del API
 */
interface ApiService {
    @GET("{username}")
    fun getClientByUsername(@Path("username") username: String): Call<List<Cliente>>

    @Headers("Content-Type: Application/json")
    @POST("/signup")
    fun createClient(@Body client: Cliente): Call<SignUpResponse>


}