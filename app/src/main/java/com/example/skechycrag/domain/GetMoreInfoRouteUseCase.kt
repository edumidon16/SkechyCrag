package com.example.skechycrag.domain

import com.example.skechycrag.data.model.user.MoreInfoRouteModel
import com.example.skechycrag.data.repository.UserRepository
import javax.inject.Inject

class GetMoreInfoRouteUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    var communityGrade: String = ""
    suspend operator fun invoke(routeName: String, bookGrade : String): List<MoreInfoRouteModel>{
        val climbersCommentsList = userRepository.getMoreInfoRoute(routeName)
        communityGrade = calculateCommunityGrade(bookGrade, climbersCommentsList)
        println(communityGrade)
        return climbersCommentsList
    }
    private  fun calculateCommunityGrade(bookGrade: String, communityGrades: List<MoreInfoRouteModel>?): String{

        val bookWeight = 0.3 // Base weight for the book grade

        var totalEffectiveLevels = 0.0
        var communityGradeSum = 0.0

        if (communityGrades != null) {
            for(climbers in communityGrades){
                totalEffectiveLevels += climbers.level
            }
            if (totalEffectiveLevels > 0) {
                for (grade in communityGrades) {
                    communityGradeSum += (convertGradeToNumber(grade.grade) * (grade.level / totalEffectiveLevels))
                }
            }
        }
        // Calculate weighted grades
        val weightedBookGrade = convertGradeToNumber(bookGrade)* bookWeight
        val communityWeight = 1 - bookWeight
        val weightedCommunityGrade = communityGradeSum * communityWeight
        return convertNumberToGrade(weightedBookGrade + weightedCommunityGrade)
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

    private fun convertNumberToGrade(number: Double): String {
        // Convert the number back to a climbing grade
        return when {
            number <= 1.0 -> "5"
            number <= 2.0 -> "5+"
            number <= 3.0 -> "6a"
            number <= 4.0 -> "6a+"
            number <= 5.0 -> "6b"
            number <= 6.0 -> "6b+"
            number <= 7.0 -> "6c"
            number <= 8.0 -> "6c+"
            number <= 9.0 -> "7a"
            number <= 10.0 -> "7a+"
            number <= 11.0 -> "7b"
            number <= 12.0 -> "7b+"
            number <= 13.0-> "7c"
            number <= 14.0-> "7c+"
            number <= 15.0-> "8a"
            // Add other cases
            else -> "NA" // default or error case
        }
    }
}