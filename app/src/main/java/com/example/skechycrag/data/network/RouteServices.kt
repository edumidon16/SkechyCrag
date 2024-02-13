package com.example.skechycrag.data.network

import com.example.skechycrag.data.model.route.RouteModel
import com.google.firebase.firestore.FirebaseFirestore
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
                    document.toObject(RouteModel::class.java)?.copy()
                }
            } catch (e: Exception) {
                // Handle any errors here
                // Log the error or print stack trace
                e.printStackTrace() // Use Log.e if in an Android environment
                emptyList<RouteModel>()
            }
        }

    }


}