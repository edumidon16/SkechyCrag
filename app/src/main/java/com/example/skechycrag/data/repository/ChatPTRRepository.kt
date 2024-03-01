package com.example.skechycrag.data.repository

import com.example.skechycrag.data.network.ChatGPTServices
import javax.inject.Inject

class ChatPTRRepository @Inject constructor(
    private val chatGPTServices: ChatGPTServices
) {
    suspend fun readImage(image: String):String {
       return chatGPTServices.readImage(image)
    }
}