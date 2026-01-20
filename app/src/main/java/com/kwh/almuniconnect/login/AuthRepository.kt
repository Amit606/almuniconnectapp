package com.kwh.almuniconnect.login

import android.util.Log
import com.google.gson.Gson
import com.kwh.almuniconnect.api.ApiResponse
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.SignupRequest
import com.kwh.almuniconnect.api.SignupResponse

// AuthRepository.kt
class AuthRepository(private val api: ApiService) {
    suspend fun signup(request: SignupRequest): Result<SignupResponse> {
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
    suspend fun checkEmailAndGetUser(
        email: String
    ): Result<ExistingUserDto?> {

        return try {
            val resp = api.checkEmailExist(email)

            if (!resp.isSuccessful) {
                return Result.failure(Exception("HTTP ${resp.code()}"))
            }

            val body = resp.body()
                ?: return Result.failure(Exception("Empty response"))

            // ✅ NOW data EXISTS
            val rawData = body.data
                ?: return Result.success(null)
Log.e("AuthRepository", "rawData: $rawData")
            val jsonObject = Gson()
                .toJsonTree(rawData)
                .asJsonObject

            // ✅ email exists → user object
            if (jsonObject.has("userId")) {
                val user = Gson().fromJson(
                    jsonObject,
                    ExistingUserDto::class.java
                )
                Result.success(user)
            } else {
                // ❌ email does not exist
                Result.success(null)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
