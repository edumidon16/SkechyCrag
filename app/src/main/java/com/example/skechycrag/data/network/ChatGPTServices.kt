package com.example.skechycrag.data.network

import android.util.Log
import com.example.skechycrag.BuildConfig
import com.example.skechycrag.data.model.route.RouteInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.serialization.json.*
import org.json.JSONArray
import org.json.JSONObject


class ChatGPTServices @Inject constructor(
    private val db: FirebaseFirestore
) {


    suspend fun addNewRoutes(cragName: String, newRoutesList: MutableList<RouteInfo>) {
        withContext(Dispatchers.IO) {
            try {
                val numberRoutes = newRoutesList.size
                val crag = hashMapOf(
                    "crag_name" to cragName,
                    "number_routes" to numberRoutes
                )
                db.collection("CragTable").add(crag)
                    .addOnSuccessListener { documentReference ->
                        // Document has been added successfully
                        println("DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                for (route in newRoutesList) {
                    val newRoute = hashMapOf(
                        "crag_name" to cragName,
                        "route_name" to route.routeName,
                        "grade" to route.grade,
                        "type" to ""
                    )
                    db.collection("RouteTable").add(newRoute)
                }
            } catch (e: Exception) {
                // Handle any errors here
                Log.e("addNewRoutes", "Error writing document", e)
            }
        }
    }
    suspend fun readImage(image: String): String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Increase connect timeout
                .readTimeout(30, TimeUnit.SECONDS) // Increase read timeout
                .writeTimeout(30, TimeUnit.SECONDS) // Increase write timeout
                .build()

            val apiKey = BuildConfig.APIKEY

            val question = "Provide a list of the route names follow by the route grade. The response should look like this: routeName - routeGrade"

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val json = buildJsonObject {
                put("model", "gpt-4-vision-preview")
                putJsonArray("messages") {
                    addJsonObject {
                        put("role", "user")
                        putJsonArray("content") {
                            addJsonObject {
                                put("type", "text")
                                put("text", question)
                            }
                            addJsonObject {
                                put("type", "image_url")
                                put("image_url", "data:image/jpeg;base64,$image")
                            }
                        }
                    }
                }
                put("max_tokens", 500)
            }.toString()

            val body = json.toRequestBody(mediaType)


            val request = Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader(
                    "Authorization",
                    "Bearer $apiKey"
                )
                .build()
            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: ""
                val jsonObject= JSONObject(responseBody)
                val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                val textResult = jsonArray.getJSONObject(0).getJSONObject("message").getString("content")

                return@withContext textResult

                }
            }

        }
    }


