package com.kwh.almuniconnect.tallent



data class Talent(
    val id: String = "",
    val name: String = "",
    val branch: String = "",
    val year: Int = 0,
    val skill: String = "",
    val videoLink: String = "",
    val userEmail: String = "",
    val rating: Float = 0f,
    val likes: Int = 0,
    val status: TalentStatus = TalentStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String = ""
) {
    val isTopRated: Boolean
        get() = rating >= 4.8f
}