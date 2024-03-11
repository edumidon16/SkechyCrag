package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.route.RouteInfo
import com.example.skechycrag.data.network.ChatGPTServices
import com.example.skechycrag.data.network.CragServices
import javax.inject.Inject

class ChatPTRRepository @Inject constructor(
    private val chatGPTServices: ChatGPTServices,
    private val cragServices: CragServices
) {
    suspend fun readImage(image: String):String {
       return chatGPTServices.readImage(image)
    }

    suspend fun addNewRoutes(cragName: String, newRoutesList: MutableList<RouteInfo>) {
        val cragInfo = cragServices.getCragInfo(cragName)
        if(cragInfo.isNullOrEmpty()){
            chatGPTServices.addNewRoutes(cragName, newRoutesList)
        }
    }
}