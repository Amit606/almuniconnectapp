package com.kwh.almuniconnect.jobposting

import com.kwh.almuniconnect.api.ApiService

class JobRepository(private val api: ApiService) {

    suspend fun getJobPosts(
        pageNumber: Int,
        pageSize: Int
    ): Result<JobPostData> {
        return try {
            val resp = api.getJobPosts(pageNumber, pageSize)
            if (resp.isSuccessful) {
                resp.body()?.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty job data"))
            } else {
                Result.failure(
                    Exception("HTTP ${resp.code()}: ${resp.errorBody()?.string()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
