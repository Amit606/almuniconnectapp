package com.kwh.almuniconnect.login

import com.kwh.almuniconnect.api.ApiResponse
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.SignupRequest

// AuthRepository.kt
class AuthRepository(private val api: ApiService) {
    suspend fun signup(request: SignupRequest): Result<ApiResponse<Any>> {
        return try {
            val resp = api.signup(request)
            if (resp.isSuccessful) {
                val body = resp.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                // try to extract error message or show code
                val msg = resp.errorBody()?.string()
                Result.failure(Exception("HTTP ${resp.code()}: $msg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
