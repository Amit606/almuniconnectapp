package com.kwh.almuniconnect.nearby

data class NearbyAlumniResponse(
    val success: Boolean,
    val data: List<AlumniNearDto>,
    val message: String?,
    val correlationId: String?,
    val errors: Any?
)

data class AlumniNearDto(
    val alumniId: String,
    val name: String,
    val courseName: String,
    val batch: Int,
    val cityName: String,
    val photoUrl: String?,
    val latitude: String,
    val longitude: String,
    val distanceKm: Double
)
