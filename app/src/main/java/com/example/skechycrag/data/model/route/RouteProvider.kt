package com.example.skechycrag.data.model.route

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteProvider @Inject constructor() {
    var routeList:List<RouteModel> = emptyList<RouteModel>()
}