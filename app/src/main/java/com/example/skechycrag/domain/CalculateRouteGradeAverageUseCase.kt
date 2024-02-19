package com.example.skechycrag.domain

import com.example.skechycrag.ui.model.MoreInfoRouteModel
import javax.inject.Inject

class CalculateRouteGradeAverageUseCase @Inject constructor() {

    suspend operator fun invoke(bookGrade: String, moreInfoList: List<MoreInfoRouteModel>?): String{
        if (moreInfoList.isNullOrEmpty()) return bookGrade

        // First, convert all grades including bookGrade to a numerical system
        val gradesNumerical = moreInfoList.mapNotNull { it.grade }.map { convertGradeToNumber(it) } + convertGradeToNumber(bookGrade)
        // Calculate the average
        val average = gradesNumerical.average()
        // Convert the numerical average back to the grade system
        return convertNumberToGrade(average)
    }

    private fun convertGradeToNumber(grade: String): Double {
        // Convert the grade to a number
        return when(grade) {
            "5" -> 1.0
            "5+" -> 1.1
            "6a" -> 1.2
            "6a+" -> 1.3
            "6b" -> 1.4
            "6b+" -> 1.5
            "6c" -> 1.6
            "6c+" -> 1.7
            "7a" -> 1.8
            "7a+" -> 1.9
            "7b" -> 2.0
            else -> 0.0 // default or error case
        }
    }

    private fun convertNumberToGrade(number: Double): String {
        // Convert the number back to a climbing grade
        return when {
            number <= 1.0 -> "5"
            number <= 1.1 -> "5+"
            number <= 1.2 -> "6a"
            number <= 1.3 -> "6a+"
            number <= 1.4 -> "6b"
            number <= 1.5 -> "6b+"
            number <= 1.6 -> "6c"
            number <= 1.7 -> "6c+"
            number <= 1.8 -> "7a"
            number <= 1.9 -> "7a+"
            number <= 2.0 -> "7b"
            // Add other cases
            else -> "NA" // default or error case
        }
    }
}