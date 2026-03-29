package com.kwh.almuniconnect.login

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.SignupRequest
import com.kwh.almuniconnect.profile.ProfileResponse
import com.kwh.almuniconnect.storage.TokenDataStore

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
        email: String,
        context: Context
    ): Result<ExistingUserDto?> {
      val tokenDataStore = TokenDataStore(context)
        return try {

           // val response = api.checkEmailExist(email)//
            val response = api.checkEmailExist("amitsun.noida@gmail.com")//
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
            tokenDataStore.saveTokens(
                accessToken = body.data?.accessToken.orEmpty(),
                refreshToken = body.data?.refreshToken.orEmpty(),
                accessTokenExpiry = body.data?.accessTokenExpiry.orEmpty(),
                refreshTokenExpiry = body.data?.refreshTokenExpiry.orEmpty()
            )

            val user = body.data?.userProfile
            if (user != null) {
                Result.success(user)
            } else {
                Result.success(null)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
