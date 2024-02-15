package com.example.skechycrag.data.network

import android.util.Log
import com.example.skechycrag.data.model.route.RouteModel
import com.example.skechycrag.data.model.user.UserRouteModel
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

    suspend fun addRouteToLogBook(user_id:String, routeId:String, route: UserRouteModel) {
        withContext(Dispatchers.IO) {
            try {
                // Add a new document with a generated ID in subcollection 'Routes' under the user's document
                db.collection("UserRoutesTable").document(user_id)
                    .collection("Routes").document(routeId)
                    .set(route)
                    .await()
                Log.d("addRouteToLogBook", "Route successfully written!")
            } catch (e: Exception) {
                // Handle any errors here
                Log.e("addRouteToLogBook", "Error writing document", e)
            }
        }
    }

}