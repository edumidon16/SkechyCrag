package com.example.skechycrag.data.network

import com.example.skechycrag.data.model.UserModel
import com.example.skechycrag.di.NetworkModule
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserServices @Inject constructor(
    private val userApi : UserApiClient,
    private val db: FirebaseFirestore
){

    suspend fun getUsers(): List<UserModel>{
        return withContext(Dispatchers.IO){
            var user = userApi.getAllUsers()
            user.body()?: emptyList()
        }
    }
    suspend fun getUsersFromFire(): List<UserModel>{
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = db.collection("UserTable").get().await()
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(UserModel::class.java)?.copy()
                }
            } catch (e: Exception) {
                // Handle any errors here
                // Log the error or print stack trace
                e.printStackTrace() // Use Log.e if in an Android environment
                emptyList<UserModel>()
            }
        }
    }
}