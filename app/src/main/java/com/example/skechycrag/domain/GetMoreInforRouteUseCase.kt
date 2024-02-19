package com.example.skechycrag.domain

import com.example.skechycrag.data.model.user.MoreInfoRouteModel
import com.example.skechycrag.data.repository.UserRepository
import javax.inject.Inject

class GetMoreInforRouteUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(routeName: String): List<MoreInfoRouteModel>{
        return userRepository.getMoreInfoRoute(routeName)
    }
}