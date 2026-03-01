package com.kwh.almuniconnect.network

import android.util.Log
import com.kwh.almuniconnect.api.ApiService

class AlumniRepository(private val api: ApiService) {

    suspend fun getAlumniList(
        pageNumber: Int,
        pageSize: Int,
        ascending: Boolean
    ): Result<AlumniListResponse> {
        return try {
            val resp = api.getAlumniList(
                pageNumber = pageNumber,
                pageSize = pageSize,
                ascending = ascending
            )

            if (resp.isSuccessful) {
                resp.body()?.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty alumni data"))
            } else {
                Result.failure(
                    Exception("HTTP ${resp.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}




