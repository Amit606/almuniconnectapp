package com.kwh.almuniconnect.nearby

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient

class NearbyRepository(
    private val api: ApiService = NetworkClient.createService(ApiService::class.java)
) {

    suspend fun getNearby(lat: Double, lng: Double): List<NearAlumni> {

        return try {

            val response = api.getNearbyAlumni(lat, lng)

            if (!response.success || response.data == null) {
                emptyList()
            } else {
                response.data.map {

                    NearAlumni(
                        userId = it.alumniId,
                        name = it.name,
                        branch = it.courseName,
                        batch = it.batch.toString(),
                        city = it.cityName,
                        profileImage = it.photoUrl,
                        latitude = it.latitude.toDoubleOrNull() ?: 0.0,
                        longitude = it.longitude.toDoubleOrNull() ?: 0.0,
                        distanceKm = it.distanceKm.toFloat()
                    )
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}