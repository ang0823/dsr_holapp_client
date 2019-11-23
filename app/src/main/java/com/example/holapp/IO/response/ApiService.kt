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

    @FormUrlEncoded
    @POST("/signup")
    fun createClient(
        @Field("nombre") nombre: String,
        @Field("apellido_p") paterno: String,
        @Field("apellido_m") materno: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<SignUpResponse>
}