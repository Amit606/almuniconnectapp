package com.kwh.almuniconnect.verification

import android.content.Context
import android.util.Log
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.api.VerifyProfileResponse
import com.kwh.almuniconnect.api.VerifyRequest
import com.kwh.almuniconnect.storage.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class AlumniRepository {

    private val api: ApiService =
        NetworkClient.createService(ApiService::class.java)

    suspend fun isAlumniVerified(alumniId: String): Boolean {
        return try {
            val response = api.checkAlumniVerification(
                alumniId = alumniId,
                correlationId = UUID.randomUUID().toString()
            )

            if (response.isSuccessful) {
                val body = response.body()
                body?.success == true && body.data == true
            } else {
                false
            }

        } catch (e: Exception) {
            false
        }
    }

    suspend fun getPending(alumniId: String): Result<List<AlumniData>> {
        return try {
            val response = api.getPendingVerifications(
                alumniId = alumniId,
                correlationId = UUID.randomUUID().toString()
            )

            if (response.isSuccessful && response.body()?.success == true) {

                val items = response.body()?.data?.items ?: emptyList()

                Result.success(items)

            } else {
                Result.failure(
                    Exception(response.body()?.message ?: "Error")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
//    suspend fun verifyAlumni(context: Context,
//        alumniId: String,
//        isVerified: Boolean
//    ): Result<VerifyProfileResponse> {
//
//        return try {
//            val tokenDataStore = TokenDataStore(context)
//            val token = tokenDataStore.getAccessToken().first()
//
//            if (token.isNullOrEmpty()) {
//                return Result.failure(Exception("Access token missing"))
//            }
//            Log.e("AlumniRepository", "Verification response token: ${token}")
//
//            val response = api.verifyAlumniProfile(
//                alumniId = alumniId,
//                body = VerifyRequest(isVerified),
//                correlationId = UUID.randomUUID().toString(),
//                authorization = "Bearer $token"   // 👈 Pass here
//            )
//            Log.e("AlumniRepository", "Verification response code: ${response.code()}")
//
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    Result.success(it)
//                } ?: Result.failure(Exception("Empty response"))
//            } else {
//                Log.e("AlumniRepository", "Verification failed with code: ${response.code()} and body: ${response.errorBody()?.string()}")
//                Result.failure(Exception("Verification failed: ${response.code()}"))
//            }
//
//        }  catch (e: Exception) {
//        Log.e("AlumniRepository", "Exception type: ${e.javaClass.simpleName}")
//        Log.e("AlumniRepository", "Exception message: ${e.message}")
//        e.printStackTrace()
//        Result.failure(e)
//    }
//    }

    suspend fun verifyAlumni(
        context: Context,
        alumniId: String,
        isVerified: Boolean
    ): Result<VerifyProfileResponse> {

        return try {

            val token = TokenDataStore(context.applicationContext)
                .getAccessToken()
                .firstOrNull()
            if (token.isNullOrEmpty()) {
                Log.e("VERIFY_DEBUG", "Token missing")
                return Result.failure(Exception("Access token missing"))
            }

            Log.e("VERIFY_DEBUG", "Token found")

            val response = api.verifyAlumniProfile(
                alumniId = alumniId,
                body = VerifyRequest(isVerified),
                correlationId = UUID.randomUUID().toString(),
                authorization = "Bearer $token"
            )

            Log.e("VERIFY_DEBUG", "Response code: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let {
                    return Result.success(it)
                }

                if (response.code() == 204) {
                    Log.e("VERIFY_DEBUG", "Received 204 No Content - treating as success")
//                    return Result.success(
//                        VerifyProfileResponse()
//                    )
                }

                return Result.failure(Exception("Empty response"))
            }

            val errorBody = response.errorBody()?.string()
            Log.e("VERIFY_ERROR", errorBody ?: "No error body")

            Result.failure(Exception("Verification failed: ${response.code()}"))

        } catch (e: Exception) {
            Log.e("VERIFY_EXCEPTION", e.message ?: "Unknown error")
            e.printStackTrace()
            Result.failure(e)
        }
    }

}
