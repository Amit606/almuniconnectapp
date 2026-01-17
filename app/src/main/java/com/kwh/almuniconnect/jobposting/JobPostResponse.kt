package com.kwh.almuniconnect.jobposting

import java.util.UUID

data class JobPostResponse(
    val success: Boolean,
    val data: JobPostData?,
    val message: String,
    val correlationId: String?,
    val errors: Any?
)

data class JobPostData(
    val items: List<JobAPost>,
    val totalCount: Int,
    val pageNumber: Int,
    val pageSize: Int
)

data class JobAPost(
    val jobId: String,
    val title: String,
    val description: String,
    val location: String,
    val employmentType: String,
    val totalExperience: String,
    val salary: String,
    val isActive: Boolean,
    val createdAtUtc: String
)

val dummyJobPosts = listOf(

    JobAPost(
        jobId = UUID.randomUUID().toString(),
        title = "Android Developer",
        description = "We are looking for an Android Developer with strong knowledge of Kotlin, Jetpack Compose, and modern Android architecture.",
        location = "Bangalore, India",
        employmentType = "Full-time",
        totalExperience = "2–4 years",
        salary = "₹8–12 LPA",
        isActive = true,
        createdAtUtc = "2026-01-10T09:30:00Z"
    ),

    JobAPost(
        jobId = UUID.randomUUID().toString(),
        title = "Backend Engineer (Java)",
        description = "Join our backend team to build scalable microservices using Java, Spring Boot, and cloud technologies.",
        location = "Hyderabad, India",
        employmentType = "Full-time",
        totalExperience = "3–6 years",
        salary = "₹10–18 LPA",
        isActive = true,
        createdAtUtc = "2026-01-08T06:45:00Z"
    ),

    JobAPost(
        jobId = UUID.randomUUID().toString(),
        title = "Product Manager",
        description = "Seeking a Product Manager to define product roadmap, work closely with engineering, and deliver customer-focused solutions.",
        location = "Remote",
        employmentType = "Full-time",
        totalExperience = "4–7 years",
        salary = "₹15–25 LPA",
        isActive = true,
        createdAtUtc = "2026-01-05T11:15:00Z"
    ),

    JobAPost(
        jobId = UUID.randomUUID().toString(),
        title = "UI/UX Designer",
        description = "Looking for a creative UI/UX Designer with experience in mobile and web design systems and user research.",
        location = "Delhi NCR, India",
        employmentType = "Contract",
        totalExperience = "2–5 years",
        salary = "₹6–10 LPA",
        isActive = true,
        createdAtUtc = "2026-01-03T08:20:00Z"
    ),

    JobAPost(
        jobId = UUID.randomUUID().toString(),
        title = "Data Analyst",
        description = "Analyze business data, create dashboards, and generate insights using SQL, Python, and BI tools.",
        location = "Pune, India",
        employmentType = "Full-time",
        totalExperience = "1–3 years",
        salary = "₹7–11 LPA",
        isActive = false,
        createdAtUtc = "2025-12-28T05:10:00Z"
    )
)