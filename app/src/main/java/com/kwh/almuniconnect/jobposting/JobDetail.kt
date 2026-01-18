package com.kwh.almuniconnect.jobposting

data class JobDetail(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val experience: String,
    val salary: String,
    val jobType: String,
    val skills: List<String>,
    val description: String,
    val responsibilities: List<String>,
    val applyEmail: String,
    val source:String = "LinkedIn"
)
