package com.kwh.almuniconnect.tallent



data class Talent(
    val id: String = "",
    val name: String = "",
    val branch: String = "",
    val year: Int = 0,
    val skill: String = "",
    val videoLink: String = "",
    val photo: String = "",
    val userEmail: String = "",
    val description: String = "",
    val rating: Float = 0f,
    val likes: Int = 0,
    val status: String = TalentStatus.PENDING.name,
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String = ""
) {
    val isTopRated: Boolean
        get() = rating >= 4.8f
}