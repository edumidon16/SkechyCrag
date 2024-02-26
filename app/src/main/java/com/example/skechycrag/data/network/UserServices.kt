package com.example.skechycrag.data.network

import com.example.skechycrag.data.model.user.MoreInfoRouteModel
import com.example.skechycrag.data.model.user.UserModel
import com.example.skechycrag.data.model.user.UserRouteModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserServices @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getUsers(username: String): UserModel? {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    db.collection("UserTable").whereEqualTo("username", username).get().await()
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
                    db.collection("UserRoutesTable").document(userId).collection("Routes").get()
                        .await()

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

    suspend fun changeUserLevel(userLevel: UserModel?) {
        try {
            userLevel?.let {
                val update = hashMapOf<String, Any>(
                    "level" to userLevel.level,
                    "difficultySum" to userLevel.difficultySum,
                    "numRoutes" to userLevel.numRoutes
                )
                val levelUpdate = hashMapOf<String, Any>(
                    "level" to userLevel.level
                )
                userLevel?.let {
                    db.collection("UserTable").document(it.user_id)
                        .update(update)
                        .await()

                    db.collection("UserRoutesTable").document(it.user_id)
                        .update(levelUpdate)
                        .await()
                }
            }
        } catch (e: Exception) {
            // If the update operation fails, handle the exception here
            e.printStackTrace()
        }
    }
    suspend fun getMoreInfoRoute(routeId: String): List<MoreInfoRouteModel> {
        return withContext(Dispatchers.IO) {
            val commentsList = mutableListOf<MoreInfoRouteModel>()
            try {
                // Get a list of all user documents in the UserRoutesTable
                val usersSnapshot = db.collection("UserRoutesTable").get().await()
                // Loop through each user document
                for (userDocument in usersSnapshot.documents) {
                    // For each user, retrieve the comments from the routeId
                    val climberLevel = userDocument.getDouble("level") ?: 0.0
                    val userId = userDocument.id
                    // Retrieve the comments for the routeId
                    val commentSnapshot = db.collection("UserRoutesTable")
                        .document(userId)
                        .collection("Comments")
                        .document(routeId)
                        .get()
                        .await()

                    // Map each comment document to a MoreInfoRouteModel object and add to the list
                    commentSnapshot.toObject(MoreInfoRouteModel::class.java)?.let { comment ->
                        // Combine the comment with the climber level information
                        val moreInfoRouteModel = MoreInfoRouteModel(
                            username = comment.username,
                            comment = comment.comment,
                            grade = comment.grade,
                            alert = comment.alert,
                            level = climberLevel
                        )
                        commentsList.add(moreInfoRouteModel)
                    }
                }
                commentsList // Return the list of comments
            } catch (e: Exception) {
                // Handle any errors here
                e.printStackTrace() // Use Log.e if in an Android environment
                emptyList<MoreInfoRouteModel>()
            }
        }
    }
}