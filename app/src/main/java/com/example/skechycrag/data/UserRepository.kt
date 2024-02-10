package com.example.skechycrag.data

import com.example.skechycrag.data.model.UserModel
import com.example.skechycrag.data.model.UserProvider
import com.example.skechycrag.data.network.UserServices
import javax.inject.Inject

//Esta es la clase que vamos a llamar desde DOMAIN, desde el USERCASE, para recoger las citas y la que decide de donde las saca
class UserRepository @Inject constructor(
    private val userServices : UserServices,
    private val userProvider: UserProvider
){


    //Se llama al API y se guarda los datos en el PROVIDER
    suspend fun getAllUsers():List<UserModel>{
        val response = userServices.getUsers()
        userProvider.user = response
        return response
    }
    suspend fun getAllUserFromFire():List<UserModel>{
       return userServices.getUsersFromFire()
    }
}