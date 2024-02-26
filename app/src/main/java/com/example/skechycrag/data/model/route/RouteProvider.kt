package com.example.skechycrag.data.model.route

import com.example.skechycrag.data.model.user.MoreInfoRouteModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteProvider @Inject constructor() {
    var routeList:MutableList<RouteModel> = mutableListOf()
    var routeIdProvide: MutableList<String> = mutableListOf()
    var climberList: MutableList<List<MoreInfoRouteModel>> = mutableListOf()
}