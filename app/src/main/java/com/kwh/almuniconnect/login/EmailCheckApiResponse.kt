package com.kwh.almuniconnect.login
data class EmailCheckApiResponse(
    val success: Boolean,
    val data: LoginData?,
    val message: String?,
    val correlationId: String?,
    val errors: Any?
)
data class ExistingUserDto(
    val userId: String,
    val name: String?,
    val email: String?,
    val mobileNo: String?,
    val dateOfBirth: String?,
    val passoutYear: Double?,
    val courseId: Double?,
    val courseName: String?,
    val countryName: String?,
    val cityName: String?,
    val companyName: String?,
    val title: String?,
    val totalExperience: Double?,
    val linkedinUrl: String?,
    val photoUrl: String?,
    val isVerified: Boolean?,
    val userType: String?
)
data class LoginData(
    val username: String?,
    val accessToken: String?,
    val accessTokenExpiry: String?,
    val refreshToken: String?,
    val refreshTokenExpiry: String?,
    val userProfile: ExistingUserDto?
)