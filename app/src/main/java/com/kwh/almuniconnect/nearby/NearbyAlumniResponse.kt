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

    val countryCode: String?,
    val mobileNo: String?,
    val email: String?,

    val dateOfBirth: String?,

    val courseId: Int?,
    val courseName: String,
    val batch: Int,

    val countryId: Int?,
    val countryName: String?,
    val cityName: String,

    val companyName: String?,
    val title: String?,
    val totalExperience: Int?,

    val linkedinUrl: String?,
    val photoUrl: String?,

    val isVerified: Boolean,

    val latitude: String,
    val longitude: String,

    val createdAtUtc: String?,
    val srNo: Int?,

    val distanceKm: Double
)
