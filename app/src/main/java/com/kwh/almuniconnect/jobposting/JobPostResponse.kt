package com.kwh.almuniconnect.jobposting

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