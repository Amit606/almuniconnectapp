package com.kwh.almuniconnect.profile

data class ProfileResponse(
    val success: Boolean,
    val data: ProfileData?,
    val message: String?,
    val correlationId: String?,
    val errors: String?
)