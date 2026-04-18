package com.kwh.almuniconnect.nearby
//
//data class NearAlumni(
//    val userId: String,
//    val name: String,
//    val branch: String,
//    val batch: String,
//    val city: String,
//    val profileImage: String?,
//    val latitude: Double,
//    val longitude: Double,
//    val distanceKm: Float
//)

data class NearAlumni(

    val userId: String,
    val name: String,

    val countryCode: String?,
    val mobileNo: String?,
    val email: String?,

    val courseId: Int?,
    val branch: String,              // courseName
    val batch: String,

    val countryId: Int?,
    val countryName: String?,
    val city: String,

    val companyName: String?,
    val title: String?,
    val totalExperience: Int?,

    val linkedinUrl: String?,
    val profileImage: String?,

    val isVerified: Boolean,

    val latitude: Double,
    val longitude: Double,

    val distanceKm: Float
)