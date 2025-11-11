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
}

