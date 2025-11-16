package com.kwh.almuniconnect.api

import retrofit2.Response
import retrofit2.http.*

data class Alumni(
    val id: String,
    val name: String,
    val branch: String,
    val passingYear: String
)

data class AlumniRequest(
    val name: String,
    val branch: String,
    val passingYear: String
)
data class MasterResponse(
    val success: Boolean,
    val data: List<MasterItem>
)

data class MasterItem(
    val id: Int,
    val name: String,
    val shortName: String
)
data class SignupRequest(
    val firstName: String,
    val lastName: String,
    val mobileNo: String,
    val email: String,
    val dateOfBirth: String,    // e.g. "1989-06-18"
    val dateOfMarriage: String, // e.g. "2017-12-10"
    val courseId: Int,
    val PassoutYear: Int,
    val companyName: String,
    val title: String,
    val countryId: Int,
    val loggedFrom: String,
    val deviceToken: String
)

// Generic ApiResponse (adjust fields if actual API differs)
data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)

interface ApiService {

    @GET("alumni")
    suspend fun getAllAlumni(): Response<List<Alumni>>

    @GET("alumni/{id}")
    suspend fun getAlumniById(@Path("id") id: String): Response<Alumni>

    @POST("alumni")
    suspend fun addAlumni(@Body alumni: AlumniRequest): Response<Alumni>

    @GET("masters/countries")
    suspend fun getCountries(): Response<MasterResponse>

    @GET("masters/courses")
    suspend fun getCourse(): Response<MasterResponse>


    @GET("masters/branches")
    suspend fun getBranches(): Response<MasterResponse>

    @GET("masters/batches")
    suspend fun getBatches(): Response<MasterResponse>


    @GET("masters/roles")
    suspend fun getRoles(): Response<MasterResponse>
    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    suspend fun signup(@Body body: SignupRequest): Response<ApiResponse<Any>>
}

