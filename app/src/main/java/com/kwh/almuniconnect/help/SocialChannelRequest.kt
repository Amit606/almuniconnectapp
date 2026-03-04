package com.kwh.almuniconnect.help

data class SocialChannelRequest(

    val title: String = "",
    val description: String = "",
    val link: String = "",
    val type: String = "",

    val status: String = "pending",

    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",

    val createdAt: Long = System.currentTimeMillis()
)