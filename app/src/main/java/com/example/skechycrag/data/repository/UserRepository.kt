package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.user.UserModel
import com.example.skechycrag.data.model.user.UserProvider
import com.example.skechycrag.data.model.user.UserRouteModel
import com.example.skechycrag.data.network.UserServices
import javax.inject.Inject

//Esta es la clase que vamos a llamar desde DOMAIN, desde el USERCASE, para recoger las citas y la que decide de donde las saca
class UserRepository @Inject constructor(
    private val userServices : UserServices,
    private val userProvider: UserProvider
){

    suspend fun getAllUsers(username: String):UserModel?{
        val user = userServices.getUsers(username)
        userProvider.user = user
        return user
    }

    suspend fun getLogBook(username: String): List<UserRouteModel> {
        if(username == userProvider.user?.username ){
            return userServices.getLogBook(userProvider.user?.user_id?: return emptyList())
        }
        return emptyList()
    }
}