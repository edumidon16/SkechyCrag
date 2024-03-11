package com.example.skechycrag.data.network

import com.example.skechycrag.data.model.crag.CragModel
import com.example.skechycrag.data.model.user.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CragServices @Inject constructor(
    private val db: FirebaseFirestore
) {


    suspend fun getCragInfo(name: String): List<CragModel> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = db.collection("CragTable")
                    .whereEqualTo("crag_name", name)
                    .get()
                    .await()
                println("Firestore returned documents: ${querySnapshot.documents}")
                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(CragModel::class.java)?.copy(crag_id = document.id)
                }
            } catch (e: Exception) {
                // Handle any errors here
                // Log the error or print stack trace
                e.printStackTrace() // Use Log.e if in an Android environment
                emptyList<CragModel>() // Return an empty list on error
            }
        }
    }

}