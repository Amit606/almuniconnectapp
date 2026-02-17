package com.kwh.almuniconnect.login
data class EmailCheckApiResponse(
    val success: Boolean,
    val data: Any?,              // 🔥 IMPORTANT (dynamic)
    val message: String?,
    val correlationId: String?,
    val errors: Any?
)
data class ExistingUserDto(
    val userId: String,
    val name: String?,
    val email: String?,
    val mobileNo: String?,
    val countryCode: String?,
    val courseId: Int?,
    val batch: Int?,
    val companyName: String?,
    val title: String?,
    val photoUrl: String?,
    val isVerified: Boolean
)
