package com.example.skechycrag.data.network

import android.util.Log
import com.example.skechycrag.data.model.route.RouteModel
import com.example.skechycrag.data.model.user.MoreInfoRouteModel
import com.example.skechycrag.data.model.user.UserRouteModel
import com.example.skechycrag.ui.constants.Constants.Companion.USERNAME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RouteServices @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun getAllRoutesFromCrag(name: String): List<RouteModel> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    db.collection("RouteTable").whereEqualTo("crag_name", name).get().await()
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(RouteModel::class.java)?.copy(route_id = document.id)
                }
            } catch (e: Exception) {
                // Handle any errors here
                // Log the error or print stack trace
                e.printStackTrace() // Use Log.e if in an Android environment
                emptyList<RouteModel>()
            }
        }

    }

    suspend fun addRouteToLogBook(
        user_id: String,
        routeId: String,
        route: UserRouteModel,
        commentRoute: MoreInfoRouteModel
    ) {
        withContext(Dispatchers.IO) {
            try {
                val commentR = hashMapOf<String, Any>(
                    "username" to commentRoute.username,
                    "comment" to commentRoute.comment,
                    "grade" to commentRoute.grade,
                    "alert" to commentRoute.alert
                )
                // Add a new document with a generated ID in subcollection 'Routes' under the user's document
                db.collection("UserRoutesTable").document(user_id)
                    .collection("Routes").document(routeId)
                    .set(route)
                    .await()

                db.collection("UserRoutesTable").document(user_id)
                    .collection("Comments").document(routeId)
                    .set(commentR)
                    .await()

                Log.d("addRouteToLogBook", "Route successfully written!")
            } catch (e: Exception) {
                // Handle any errors here
                Log.e("addRouteToLogBook", "Error writing document", e)
            }
        }
    }

    suspend fun addAlert(userId: String?, alertMessage: String, routeId: String) {
        withContext(Dispatchers.IO) {
            try {
                if (userId != null) {
                    val routeDocumentRef = db.collection("UserRoutesTable").document(userId)
                        .collection("Comments").document(routeId)

                    val snapshot = routeDocumentRef.get().await()
                    if (snapshot.exists()) {
                        // The route document exists, update the alert data
                        routeDocumentRef.update("alert", alertMessage).await()
                    } else {
                        val newCommentData = hashMapOf(
                            "username" to USERNAME,
                            "alert" to alertMessage,
                            "grade" to "", // Assuming default values, update as necessary
                            "comment" to "" // Assuming default values, update as necessary
                        )
                        routeDocumentRef.set(newCommentData).await()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun addNewRoute(
        cragName: String,
        routeName: String,
        routeGrade: String,
        type: String,
        cragId: String,
        numRoutes: Int
    ) {
        var id = ""
        withContext(Dispatchers.IO) {
            try {
                // Check if cragId is null or empty, or if numRoutes is 0
                if (cragId.isNullOrEmpty() || numRoutes == 0) {
                    val querySnapshot = db.collection("CragTable")
                        .whereEqualTo("crag_name", cragName)
                        .get()
                        .await()

                    val document = if (querySnapshot.documents.isEmpty()) {
                        // Crag doesn't exist, create a new crag
                        val newCragData = hashMapOf(
                            "crag_name" to cragName,
                            "number_routes" to 1 // Starting with 1 route
                        )
                        db.collection("CragTable").add(newCragData).await()
                    } else {
                        // Crag exists, update number of routes
                        val existingCragDoc = querySnapshot.documents.first()
                        val updatedRoutesCount =
                            existingCragDoc.getLong("number_routes")?.plus(1) ?: 1
                        db.collection("CragTable").document(existingCragDoc.id)
                            .update("number_routes", updatedRoutesCount).await()
                        existingCragDoc
                    }
                }

                val newRoute = hashMapOf(
                    "crag_name" to cragName,
                    "route_name" to routeName,
                    "grade" to routeGrade,
                    "type" to type
                )
                // Add the new route and await the result
                val documentReference = db.collection("RouteTable").add(newRoute).await()

                // Return the new document ID
                documentReference.id
            } catch (e: Exception) {
                // Handle any errors here
                Log.e("addNewRoutes", "Error writing document", e)
            }
        }

    }

}