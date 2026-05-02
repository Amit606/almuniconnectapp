package com.kwh.almuniconnect.nearby
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

                        countryCode = it.countryCode,
                        mobileNo = it.mobileNo,
                        email = it.email,

                        courseId = it.courseId,
                        branch = it.courseName,
                        batch = it.batch.toString(),

                        countryId = it.countryId,
                        countryName = it.countryName,
                        city = it.cityName,

                        companyName = it.companyName,
                        title = it.title,
                        totalExperience = it.totalExperience,

                        linkedinUrl = it.linkedinUrl,
                        profileImage = it.photoUrl,

                        isVerified = it.isVerified,

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