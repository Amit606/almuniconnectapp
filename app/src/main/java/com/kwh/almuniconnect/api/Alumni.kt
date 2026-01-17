package com.kwh.almuniconnect.api

import com.kwh.almuniconnect.evetns.EventsResponse
import com.kwh.almuniconnect.jobposting.JobPostResponse
import com.kwh.almuniconnect.news.NewsResponse
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
    val name: String,
    val mobileNo: String,
    val email: String,
    val dateOfBirth: String,
    val passoutYear: Int,
    val courseId: Int,
    val countryId: Int,
    val companyName: String,
    val title: String,
    val totalExperience: Int,
    val linkedinUrl: String,
    val loggedFrom: String,
    val deviceId: String,
    val fcmToken: String,
    val appVersion: String,
    val advertisementId: String,
    val userAgent: String
)
data class SignupResponse(
    val success: Boolean,
    val data: UserData?,
    val message: String,
    val correlationId: String?,
    val errors: Any?
)

data class UserData(
    val userId: String
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


    @GET("events")
    suspend fun getEvents(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): EventsResponse

    @GET("job-posts")
    suspend fun getJobPosts(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<JobPostResponse>

    @GET("news")
    suspend fun getNews(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<NewsResponse>

    @GET("masters/roles")
    suspend fun getRoles(): Response<MasterResponse>
    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    suspend fun signup(@Body body: SignupRequest): Response<SignupResponse>
}

