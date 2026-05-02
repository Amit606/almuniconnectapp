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
    val jobNo: String?,
    val title: String,
    val description: String,
    val location: String?,
    val employmentType: String?,
    val totalExperience: String?,
    val salary: String?,
    val mustHaveSkills: String?,
    val goodToHaveSkills: String?,
    val applyNowLink: String?,
    val referenceEmail: String?,
    val alumniId: String?,
    val alumniName: String?,
    val courseName: String?,
    val batch: String?,
    val photoUrl: String?,
    val isActive: Boolean,
    val createdAtUtc: String
)

