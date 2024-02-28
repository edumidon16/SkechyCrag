package com.example.skechycrag.domain

import com.example.skechycrag.data.model.route.RouteModel
import com.example.skechycrag.data.model.user.MoreInfoRouteModel
import com.example.skechycrag.data.model.user.UserRouteModel
import com.example.skechycrag.data.repository.RouteRepository
import com.example.skechycrag.data.repository.UserRepository
import com.example.skechycrag.ui.constants.Constants.Companion.USERNAME
import javax.inject.Inject

class GetRouteDetailUseCase @Inject constructor(
    private val routeRepository: RouteRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String): List<RouteModel> {
        return routeRepository.getAllRoutesFromCrag(name)
    }
    suspend fun addRoute(route: UserRouteModel){
        val commentRoute = MoreInfoRouteModel(
            username = USERNAME,
            comment = route.comment,
            grade = route.grade,
            alert = ""
        )
        var user = userRepository.getUserLevel()
        user.let {
            var difficultSum = user?.difficultySum
            var numRoutes = user?.numRoutes?.plus(1)
            var gradeInDouble = convertGradeToNumber(route.grade)
            difficultSum = difficultSum?.plus( gradeInDouble.toInt())

            //We multiplicate the difficulty sum to the number of routes that has done - 1
            //If a user has 27.0 of difficultySum with 3 routes, is equal to 26.19 of level
            //If he does a 5 = 1 point, his difficultySum will be 28.0 with 4 routes, the level = 26.88
            //Meaning that even if a good climber does a easy climb his level steal improves
            var a = 1.0
            var b = numRoutes?.div(100)?.toDouble()
            var level = difficultSum?.times(a - b!!)

            if(level?.toDouble() != 0.0){
                user?.level = level!!.toDouble()
                user?.numRoutes = numRoutes!!
                user?.difficultySum = difficultSum!!.toInt()
            }
            routeRepository.addRouteToLogBook(route, commentRoute)
            userRepository.changeUserLevel(user)
        }
    }
    private fun convertGradeToNumber(grade: String): Double {
        // Convert the grade to a number
        return when(grade) {
            "5" -> 1.0
            "5+" -> 2.0
            "6a" -> 3.0
            "6a+" -> 4.0
            "6b" -> 5.0
            "6b+" -> 6.0
            "6c" -> 7.0
            "6c+" -> 8.0
            "7a" -> 9.0
            "7a+" -> 10.0
            "7b" -> 11.0
            "7b+" -> 12.0
            "7c" -> 13.0
            "7c+" -> 14.0
            "8a" -> 15.0
            else -> 0.0 // default or error case
        }
    }
}