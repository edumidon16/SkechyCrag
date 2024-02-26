package com.example.skechycrag.data.model.user

import javax.inject.Inject
import javax.inject.Singleton

//Singleton = Se va a crear una sola instancia
@Singleton
class UserProvider @Inject constructor() {
     var user:UserModel? = null
     var userLevelList: MutableList<Double> = mutableListOf()
}