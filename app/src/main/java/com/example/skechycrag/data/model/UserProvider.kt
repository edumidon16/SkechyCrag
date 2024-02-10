package com.example.skechycrag.data.model

import javax.inject.Inject
import javax.inject.Singleton

//Singleton = Se va a crear una sola instancia
@Singleton
class UserProvider @Inject constructor() {
    var user:List<UserModel> = emptyList()

}