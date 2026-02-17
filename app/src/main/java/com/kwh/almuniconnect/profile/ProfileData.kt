package com.kwh.almuniconnect.profile

data class ProfileData(
    val username: String?,
    val accessToken: String?,
    val accessTokenExpiry: String?,
    val refreshToken: String?,
    val refreshTokenExpiry: String?,
    val userProfile: UserProfile?
)
