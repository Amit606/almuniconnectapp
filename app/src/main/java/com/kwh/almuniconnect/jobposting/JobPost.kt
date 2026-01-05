package com.kwh.almuniconnect.jobposting

data class JobPost(
    val title: String,
    val company: String,
    val location: String,
    val experience: String,
    val salary: String,
    val jobType: String,
    val skills: String,
    val description: String,
    val applyEmail: String,
    val websiteUrl: String,
    val linkedinUrl: String
)