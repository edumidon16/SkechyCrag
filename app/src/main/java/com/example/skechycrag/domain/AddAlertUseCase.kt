package com.example.skechycrag.domain

import com.example.skechycrag.data.repository.RouteRepository
import javax.inject.Inject

class AddAlertUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(alertMessage : String, routeName:String){
        routeRepository.addAlert(alertMessage, routeName)
    }
}