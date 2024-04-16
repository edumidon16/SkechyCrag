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

    suspend fun addRoute(route: UserRouteModel) {
        val commentRoute = MoreInfoRouteModel(
            username = USERNAME,
            comment = route.comment,
            grade = route.grade,
            alert = ""
        )
        var user = userRepository.getUserLevel()
        user.let {
            // Update difficulty sum and number of routes
            var difficultySum = user?.difficultySum ?: 0
            var numRoutes = user?.numRoutes ?: 0
            val gradeInDouble = convertGradeToNumber(route.grade)
            difficultySum += gradeInDouble.toInt()
            numRoutes += 1

            /*
            DifficultySum: The total sum of the difficulties of all routes the user has climbed. Difficulty for each route is converted into a numerical value that can be summed.
            NumRoutes: The total number of routes the user has climbed.
            A, B, and C are constants that need to be adjusted based on the desired scaling of the level. They allow for tuning the impact of difficulty sum and number of routes on the overall level.
            The +1 inside the logarithm ensures that the argument is always positive, which is required as the logarithm of non-positive numbers is undefined.
            The logarithmic function (log) is used to moderate the rate of level increase, ensuring that while both difficulty sum and the number of routes positively contribute to the level, the rate of increase slows down as the values grow, which simulates realistic progression.
             */
            // Constants for the level calculation formula
            val a = 10.0
            val b = 1.0
            val c = 1.0

            // Calculate the new level using the proposed formula
            val level = a * kotlin.math.ln(b * difficultySum + c * numRoutes + 1)
            // Update the user's stats
            user?.level = level
            user?.numRoutes = numRoutes
            user?.difficultySum = difficultySum

            // Persist changes
            routeRepository.addRouteToLogBook(route, commentRoute)
            userRepository.changeUserLevel(user)
        }
    }

    private fun convertGradeToNumber(grade: String): Double {
        // Convert the grade to a number
        return when (grade) {
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