package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.route.RouteModel
import com.example.skechycrag.data.model.route.RouteProvider
import com.example.skechycrag.data.network.RouteServices
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val routeServices : RouteServices,
    private val routeProvider: RouteProvider
) {

    suspend fun getAllRoutesFromCrag(name:String): List<RouteModel>{
        return routeServices.getAllRoutesFromCrag(name)
    }
}