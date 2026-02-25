package com.kwh.almuniconnect.login

import android.util.Log
import com.google.gson.Gson
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.SignupRequest
import com.kwh.almuniconnect.profile.ProfileResponse

class AuthRepository(
    private val api: ApiService
) {

    /* ------------------------------------------------ */
    /* ---------------- SIGNUP / PROFILE -------------- */
    /* ------------------------------------------------ */

    suspend fun signup(
        request: SignupRequest
    ): Result<ProfileResponse> {

        return try {

            val response = api.signup(request)

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {
                    Log.e("AuthRepository", "Signup Success: $body")
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response body"))
                }

            } else {

                val errorMsg = response.errorBody()?.string()

                Log.e(
                    "AuthRepository",
                    "Signup Failed: ${response.code()} $errorMsg"
                )

                Result.failure(
                    Exception("HTTP ${response.code()}: $errorMsg")
                )
            }

        } catch (e: Exception) {

            Log.e("AuthRepository", "Signup Exception: ${e.message}")

            Result.failure(e)
        }
    }


    /* ------------------------------------------------ */
    /* ------------- CHECK EMAIL EXIST ---------------- */
    /* ------------------------------------------------ */

    suspend fun checkEmailAndGetUser(
        email: String
    ): Result<ExistingUserDto?> {

        return try {

            val response = api.checkEmailExist("amitsun.noida@gmail.com")

            Log.e("AuthRepository", "Response: ${response.code()} ${response.body()}")

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception("HTTP ${response.code()}")
                )
            }

            val body = response.body()
                ?: return Result.failure(Exception("Empty response"))

            if (!body.success) {
                return Result.failure(Exception(body.message ?: "Unknown error"))
            }

            val user = body.data?.userProfile

            if (user != null) {
                Log.e("AuthRepository", "Existing user found: ${user.userId}")
                Result.success(user)
            } else {
                Log.e("AuthRepository", "User not found")
                Result.success(null)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
