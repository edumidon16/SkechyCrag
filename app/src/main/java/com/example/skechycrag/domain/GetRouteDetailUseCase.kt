package com.example.skechycrag.domain

import com.example.skechycrag.data.model.route.RouteModel
import com.example.skechycrag.data.model.user.UserRouteModel
import com.example.skechycrag.data.repository.RouteRepository
import javax.inject.Inject

class GetRouteDetailUseCase @Inject constructor(
    private val repository: RouteRepository
) {
    suspend operator fun invoke(name: String): List<RouteModel> {
        return repository.getAllRoutesFromCrag(name)
    }

    suspend fun addRouteToLogBook(route: UserRouteModel) {
        repository.addRouteToLogBook(route)
    }
}