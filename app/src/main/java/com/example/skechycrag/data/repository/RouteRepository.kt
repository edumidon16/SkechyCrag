package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.route.RouteModel
import com.example.skechycrag.data.model.route.RouteProvider
import com.example.skechycrag.data.model.user.MoreInfoRouteModel
import com.example.skechycrag.data.model.user.UserProvider
import com.example.skechycrag.data.model.user.UserRouteModel
import com.example.skechycrag.data.network.RouteServices
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val routeServices: RouteServices,
    private val routeProvider: RouteProvider,
    private val userProvider: UserProvider
) {

    suspend fun getAllRoutesFromCrag(name: String): List<RouteModel> {
        var cragRouteList = routeServices.getAllRoutesFromCrag(name)
        for(route in cragRouteList){
            routeProvider.routeList.add(route)
        }
        return cragRouteList
    }

    suspend fun addRouteToLogBook(route: UserRouteModel, commentRoute: MoreInfoRouteModel) {
        val userId = userProvider.user?.user_id
        var routeId = ""
        val routeName = route.route_name
        for(routes in routeProvider.routeList){
            if(routeName == routes.route_name){
                routeId = routes.route_id
            }
        }
        if(userId != null && routeId != ""){
            routeServices.addRouteToLogBook(userId,routeId, route, commentRoute)
        }
    }

    suspend fun addAlert(alertMessage: String, routeName: String) {
        val userId = userProvider.user?.user_id
        var routeId = ""
        var routeIndex = -1 // Initialize with an invalid index

        // Find the routeId by matching routeName
        for (route in routeProvider.routeList) {
            if (routeName == route.route_name) {
                routeId = route.route_id
                break // Found the matching route, no need to continue looping
            }
        }

        // If routeId is found, find the index in routeIdProvide
        if (routeId.isNotEmpty()) {
            routeIndex = routeProvider.routeIdProvide.indexOf(routeId)
        }

        // Check if a valid index was found
        if (routeIndex != -1) {
            // Assuming climberList at the found index is the list of MoreInfoRouteModel for the routeId
            val climbers = routeProvider.climberList[routeIndex] // Get the list of climbers for the route
            // Update the alert for each MoreInfoRouteModel in the list
            for (climber in climbers) {
                climber.alert = alertMessage // Update the alert message
                // Optionally, you can add more logic here if needed
            }
        }

        // Call the service to add the alert to the database
        routeServices.addAlert(userId, alertMessage, routeId)
    }

}