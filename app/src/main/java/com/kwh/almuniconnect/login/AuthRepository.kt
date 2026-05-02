package com.kwh.almuniconnect.login

import android.content.Context
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.SignupRequest
import com.kwh.almuniconnect.profile.ProfileResponse
import com.kwh.almuniconnect.storage.TokenDataStore

class AuthRepository(
    private val api: ApiService,
    private val tokenDataStore: TokenDataStore

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
                    Result.success(body)
                } else {
                    Result.failure(Exception("Empty response body"))
                }

            } else {

                val errorMsg = response.errorBody()?.string()



                Result.failure(
                    Exception("HTTP ${response.code()}: $errorMsg")
                )
            }

        } catch (e: Exception) {


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

            val response = api.checkEmailExist(email) // ✅ use param

            if (!response.isSuccessful) {
                return Result.failure(
                    Exception("HTTP ${response.code()} - ${response.message()}")
                )
            }

            val body = response.body()
                ?: return Result.failure(Exception("Empty response"))

            if (!body.success) {
                return Result.failure(
                    Exception(body.message ?: "Something went wrong")
                )
            }

            val data = body.data

            // ✅ Save tokens only if available
            if (data?.accessToken != null && data.refreshToken != null) {

                tokenDataStore.saveTokens(
                    accessToken = data.accessToken,
                    refreshToken = data.refreshToken,
                    accessTokenExpiry = data.accessTokenExpiry?.toLongOrNull()
                        ?: System.currentTimeMillis(),
                    refreshTokenExpiry = data.refreshTokenExpiry?.toLongOrNull()
                        ?: System.currentTimeMillis()
                )
            }

            Result.success(data?.userProfile)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
