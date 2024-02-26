package com.example.skechycrag.data.model.user

data class UserModel(
    val user_id: String = "",
    val username: String = "",
    val password: String = "",
    var level: Double = 0.0,
    var difficultySum: Int = 0,
    var numRoutes: Int = 0
)
