package com.example.skechycrag.domain

import com.example.skechycrag.data.model.user.UserRouteModel
import com.example.skechycrag.data.repository.UserRepository
import javax.inject.Inject

class GetLogBookUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(username:String): List<UserRouteModel>{
        return userRepository.getLogBook(username)
    }
}