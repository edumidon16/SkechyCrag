package com.example.skechycrag.data.network

import com.example.skechycrag.data.model.UserModel
import retrofit2.Response
import retrofit2.http.GET

interface UserApiClient {

    //Recupera el listado de la base de datos
    @GET("/.json")
    suspend fun getAllUsers(): Response<List<UserModel>>
}