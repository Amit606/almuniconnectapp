package com.kwh.almuniconnect.network

// AlumniListResponse.kt
data class AlumniApiResponse(
    val success: Boolean,
    val data: AlumniListResponse?,
    val message: String?,
    val correlationId: String?,
    val errors: Any?
)
data class AlumniListResponse(
    val items: List<AlumniDto>?,   // nullable (backend safe)
    val totalCount: Int,
    val pageNumber: Int,
    val pageSize: Int
)

// AlumniDto.kt
data class AlumniDto(
    val alumniId: String,
    val name: String,
    val countryCode: String?,
    val mobileNo: String?,
    val email: String?,
    val dateOfBirth: String?,
    val courseId: Int,
    val courseName: String?,
    val batch: Int,
    val countryId: Int,
    val countryName: String?,
    val companyName: String?,
    val title: String?,
    val totalExperience: Int,
    val linkedinUrl: String?,
    val photoUrl: String?,
    val isVerified: Boolean,
    val createdAtUtc: String,
    val srNo: Int
)

