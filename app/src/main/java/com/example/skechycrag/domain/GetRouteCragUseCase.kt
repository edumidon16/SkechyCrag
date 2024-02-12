package com.example.skechycrag.domain

import com.example.skechycrag.data.repository.RouteRepository
import javax.inject.Inject

class GetRouteCragUseCase @Inject constructor(
    private val repository: RouteRepository
) {
    suspend operator fun invoke(name:String){
        repository.getAllRootsFromCrag(name)
    }
}