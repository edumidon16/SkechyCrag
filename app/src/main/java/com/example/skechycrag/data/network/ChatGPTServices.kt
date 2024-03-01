package com.example.skechycrag.data.network

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class ChatGPTServices @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val client = OkHttpClient()

    suspend fun readImage(image: String): String {

        return withContext(Dispatchers.IO) {
            val question = "Extract and list only the crag names, route names, and grades from the image, starting immediately with the names and grades, without any introductions or additional commentary.\n" +
                    "The response should look like this:\n" +
                    "routeName - grade\n" +
                    "routeName2 - grade2\n"
            val apiKey = "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"
            val url = "https://api.openai.com/v1/chat/completions"

            val requestBody = """
            {
                "model": "gpt-4-vision-preview", 
                "messages": [
                    {
                        "role": "user", 
                        "content": [
                            {"type": "text", "text": "$question"},
                            {"type": "data", "data": "$image"}
                        ]
                    }
                ],
                "max_tokens": 500 
            }
            """.trimIndent()

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $apiKey")
                .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

            try {
                val response =
                    client.newCall(request).execute() // Execute the request synchronously
                val body = response.body?.string()

                if (body != null) {
                    val jsonObject = JSONObject(body)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                    val textResult =
                        jsonArray.getJSONObject(0).getJSONObject("message").getString("content")
                    return@withContext textResult // Return the text result
                } else {
                    Log.v("data", "empty")
                    return@withContext "No response from API"
                }
            } catch (e: Exception) {
                Log.e("error", "API call failed", e)
                return@withContext "Error: ${e.message}"
            }
        }

    }
}
