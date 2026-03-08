package com.kwh.almuniconnect.nearby

data class NearAlumni(
    val userId: String = "",
    val name: String = "",
    val branch: String = "",
    val batch: String = "",
    val city: String = "",
    val profileImage: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distanceKm: Float = 0f
)