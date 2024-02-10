package com.example.skechycrag.domain

import com.example.skechycrag.data.UserRepository
import com.example.skechycrag.data.model.UserModel
import javax.inject.Inject


class GetUsersUseCase @Inject constructor(
    private val repository : UserRepository
) {

    //INVOKE se llama directamente al iniciar la clase. EJ= val getUserUseCase = GetUsersUseCase() -> getUserUseCase()
    suspend operator fun invoke(username:String, password:String): Boolean{
        var users = repository.getAllUserFromFire()
        for(user in users){
            if(user.username == username && user.password == password) return true
        }
        return false
    }
}