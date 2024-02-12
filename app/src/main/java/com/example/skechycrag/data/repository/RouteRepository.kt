package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.route.RoutetModel
import com.example.skechycrag.data.model.route.RouteProvider
import com.example.skechycrag.data.network.RouteServices
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val routeServices : RouteServices,
    private val routeProvider: RouteProvider
) {

    suspend fun getAllRootsFromCrag(name:String): List<RoutetModel>{
        return routeServices.getAllRootsFromCrag(name)
    }
}