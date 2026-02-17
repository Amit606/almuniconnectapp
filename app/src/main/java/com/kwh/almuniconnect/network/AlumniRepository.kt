package com.kwh.almuniconnect.network

import android.util.Log
import com.kwh.almuniconnect.api.ApiService

class AlumniRepository(private val api: ApiService) {

    suspend fun getAlumniList(
        pageNumber: Int,
        pageSize: Int
    ): Result<AlumniListResponse> {
        return try {
            val resp = api.getAlumniList(pageNumber, pageSize)

            if (resp.isSuccessful) {
                resp.body()?.data?.let { data ->
                    Result.success(data)
                } ?: Result.failure(
                    Exception("Empty alumni data")
                )
            } else {
                Result.failure(
                    Exception(
                        "HTTP ${resp.code()}: ${resp.errorBody()?.string()}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}




