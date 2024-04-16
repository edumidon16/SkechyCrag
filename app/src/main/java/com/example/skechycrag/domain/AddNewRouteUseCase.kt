package com.example.skechycrag.domain

import com.example.skechycrag.data.repository.RouteRepository
import javax.inject.Inject

class AddNewRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {

    suspend operator fun invoke(cragName:String, routeName: String, routeGrade: String, type:String){
        routeRepository.addNewRoute(cragName, routeName, routeGrade, type)
    }
}