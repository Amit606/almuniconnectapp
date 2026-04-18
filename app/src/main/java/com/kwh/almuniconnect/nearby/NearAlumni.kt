package com.kwh.almuniconnect.nearby

data class NearAlumni(
    val userId: String,
    val name: String,
    val branch: String,
    val batch: String,
    val city: String,
    val profileImage: String?,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Float
)