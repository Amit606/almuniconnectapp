package com.kwh.almuniconnect.model

data class TokenModel(
    val accessToken: String?,
    val refreshToken: String?,
    val accessExpiry: Long?,
    val refreshExpiry: Long?
)