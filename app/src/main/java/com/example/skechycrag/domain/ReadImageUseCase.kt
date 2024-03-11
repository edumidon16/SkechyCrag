package com.example.skechycrag.domain

import com.example.skechycrag.data.model.route.RouteInfo
import com.example.skechycrag.data.repository.ChatPTRRepository
import javax.inject.Inject

class ReadImageUseCase @Inject constructor(
    private val chatGPTRepository: ChatPTRRepository
){
    suspend operator fun invoke(image: String) : MutableList<RouteInfo>{
        var response = chatGPTRepository.readImage(image)
        return parseRoutes(response)
    }
    private fun parseRoutes(response: String): MutableList<RouteInfo> {
        return response
            .lineSequence() // Create a sequence of lines from the response
            .filter { it.isNotEmpty() } // Filter out any empty lines
            .map { line ->
                val parts = line.split(" - ")
                if (parts.size >= 2) {
                    // If there are at least two parts, assume the last part is the grade
                    val routeName = parts.dropLast(1).joinToString(" - ") // Join all parts except the last with " - "
                    val grade = parts.last()
                    RouteInfo(routeName.trim(), grade.trim())
                } else {
                    null // If the line doesn't contain " - ", it's not a valid route info
                }
            }
            .filterNotNull() // Remove any null values resulting from invalid lines
            .toMutableList() // Convert the sequence to a list
    }

    suspend fun addNewRoutes(cragName: String, newRoutesList: MutableList<RouteInfo>) {
        chatGPTRepository.addNewRoutes(cragName, newRoutesList)
    }

}