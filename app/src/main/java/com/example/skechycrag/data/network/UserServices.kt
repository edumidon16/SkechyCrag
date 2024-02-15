package com.example.skechycrag.data.network

import com.example.skechycrag.data.model.route.RouteModel
import com.example.skechycrag.data.model.user.UserModel
import com.example.skechycrag.data.model.user.UserRouteModel
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserServices @Inject constructor(
    private val db: FirebaseFirestore
){

    suspend fun getUsers(username : String): UserModel?{
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = db.collection("UserTable").whereEqualTo("username", username).get().await()
                val users = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(UserModel::class.java)?.copy(user_id = document.id)
                }
                users.firstOrNull() // Return the first user if exists, or null if the list is empty
            } catch (e: Exception) {
                // Handle any errors here
                // Log the error or print stack trace
                e.printStackTrace() // Use Log.e if in an Android environment
                null // Return null in case of an error
            }
        }
    }

    suspend fun getLogBook(userId: String): List<UserRouteModel> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    db.collection("UserRoutesTable").document(userId).collection("Routes").get().await()

                println("Firestore returned documents: ${querySnapshot.documents}")
                // Map each document to a UserRouteModel object, adding the document ID as route_id
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(UserRouteModel::class.java)?.copy()

                }
            } catch (e: Exception) {
                // Handle any errors here
                // Log the error or print stack trace
                e.printStackTrace() // Use Log.e if in an Android environment
                emptyList<UserRouteModel>()
            }
        }
    }
}