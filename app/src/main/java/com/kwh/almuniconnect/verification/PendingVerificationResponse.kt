package com.kwh.almuniconnect.verification


data class PendingVerificationResponse(
    val success: Boolean,
    val data: PendingDataWrapper?,
    val message: String?,
    val correlationId: String?,
    val errors: String?
)
data class VerifyResponse(
    val success: Boolean,
    val data: Boolean,
    val message: String,
    val correlationId: String,
    val errors: Any?
)
data class PendingDataWrapper(
    val items: List<AlumniData>?,
    val totalCount: Int?,
    val pageNumber: Int?,
    val pageSize: Int?
)

data class AlumniData(
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
