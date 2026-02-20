package com.kwh.almuniconnect.verification

import android.content.Context
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.api.VerifyProfileResponse
import com.kwh.almuniconnect.api.VerifyRequest
import com.kwh.almuniconnect.storage.TokenDataStore
import kotlinx.coroutines.flow.first
import java.util.UUID

class AlumniRepository {

    private val api: ApiService =
        NetworkClient.createService(ApiService::class.java)

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
    suspend fun verifyAlumni(context: Context,
        alumniId: String,
        isVerified: Boolean
    ): Result<VerifyProfileResponse> {

        return try {
            val tokenDataStore = TokenDataStore(context)
            val token = tokenDataStore.getAccessToken().first()

            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Access token missing"))
            }

            val response = api.verifyAlumniProfile(
                alumniId = alumniId,
                body = VerifyRequest(isVerified),
                correlationId = UUID.randomUUID().toString(),
                authorization = "Bearer $token"   // 👈 Pass here
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Verification failed: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
