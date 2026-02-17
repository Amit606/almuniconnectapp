package com.kwh.almuniconnect.storage

data class UserLocalModel(

    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val mobile: String = "",
    val branch: String = "",
    val branchId: Int = 0,
    val year: String = "",
    val job: String = "",
    val location: String = "",
    val birthday: String = "",
    val linkedin: String = "",
    val photo: String = "",
    val totalExp: Int = 0,
    val accessToken: String = "",
    val accessTokenExpiry: String = "",
    val refreshToken: String = "",
    val refreshTokenExpiry: String = ""
)
