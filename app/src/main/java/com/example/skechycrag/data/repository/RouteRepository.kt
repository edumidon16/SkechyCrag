package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.crag.CragProvide
import com.example.skechycrag.data.model.route.RouteModel
import com.example.skechycrag.data.model.route.RouteProvider
import com.example.skechycrag.data.model.user.MoreInfoRouteModel
import com.example.skechycrag.data.model.user.UserProvider
import com.example.skechycrag.data.model.user.UserRouteModel
import com.example.skechycrag.data.network.RouteServices
import com.example.skechycrag.ui.constants.Constants.Companion.USERNAME
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val routeServices: RouteServices,
    private val routeProvider: RouteProvider,
    private val userProvider: UserProvider,
    private val cragProvide: CragProvide
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
            val climbers = routeProvider.climberList[routeIndex]
            for (climber in climbers) {
                if( climber.username == USERNAME){
                    climber.alert = alertMessage
                }
            }
        }
        routeServices.addAlert(userId, alertMessage, routeId)
    }

    suspend fun addNewRoute(cragName:String, routeName:String, routeGrade:String, type:String){
        var routeId = ""
        var cragId = ""
        var numRoutes = 0
        for(crag in cragProvide.cragInfo){
            if(cragName == crag.crag_name){
                crag.number_routes++
                numRoutes = crag.number_routes
                cragId = crag.crag_id
            }
        }
        routeId = routeServices.addNewRoute(cragName,routeName,routeGrade,type, cragId, numRoutes).toString()

        var newRoute = RouteModel(
            route_id = routeId,
            crag_name = cragName,
            route_name = routeName,
            grade = routeGrade,
            type = type
        )
        routeProvider.routeList.add(newRoute)
    }

}