package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.user.UserModel
import com.example.skechycrag.data.model.user.UserProvider
import com.example.skechycrag.data.network.UserServices
import javax.inject.Inject

//Esta es la clase que vamos a llamar desde DOMAIN, desde el USERCASE, para recoger las citas y la que decide de donde las saca
class UserRepository @Inject constructor(
    private val userServices : UserServices,
    private val userProvider: UserProvider
){

    suspend fun getAllUsers():List<UserModel>{
       return userServices.getUsers()
    }

}