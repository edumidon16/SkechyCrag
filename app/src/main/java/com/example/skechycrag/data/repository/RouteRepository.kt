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
}