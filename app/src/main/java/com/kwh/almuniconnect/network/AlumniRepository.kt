package com.kwh.almuniconnect.network

import android.util.Log
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.BatchCount

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
    suspend fun getAlumniBatchCount(
        courseId: Int
    ): Result<List<BatchCount>> {

        return try {

            val resp = api.getAlumniCount(courseId)

            if (resp.isSuccessful) {

                val body = resp.body()
                Log.e("Response", "Success: ${body?.success}, Message: ${body?.message}, Data: ${body?.data}")

                if (body?.success == true) {
                    Result.success(body.data)
                } else {
                    Result.failure(Exception(body?.message ?: "Invalid response"))
                }

            } else {
              Log.e("Response", "HTTP Error ${resp.code()}: ${resp.errorBody()?.string()}")
                Result.failure(Exception("HTTP Error ${resp.code()}"))

            }

        } catch (e: Exception) {
            Log.e("Response", "HTTP Error ${e.toString()}")

            Result.failure(e)

        }
    }
}







