package com.kwh.almuniconnect.profile

data class UserProfile(
    val userId: String?,
    val name: String?,
    val mobileNo: String?,
    val email: String?,
    val dateOfBirth: String?,
    val passoutYear: Int?,
    val courseId: Int?,
    val courseName: String?,
    val cityName:String?,
    val companyName: String?,
    val title: String?,
    val totalExperience: Int?,
    val linkedinUrl: String?,
    val photoUrl: String?,
    val loggedFrom: String?,
    val deviceId: String?,
    val fcmToken: String?,
    val appVersion: String?,
    val advertisementId: String?,
    val userAgent: String?,
    val isVerified: Boolean?,
    val createdAt: String?
)
