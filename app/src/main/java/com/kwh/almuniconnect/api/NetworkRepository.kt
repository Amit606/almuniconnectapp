package com.kwh.almuniconnect.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkRepository(private val api: ApiService) {


    suspend fun fetchCountries(): List<MasterItem>? = fetch { api.getCountries() }
    suspend fun fetchBranches(): List<MasterItem>? = fetch { api.getBranches() }
    suspend fun fetchBatches(): List<MasterItem>? = fetch { api.getBatches() }
    suspend fun fetchRoles(): List<MasterItem>? = fetch { api.getRoles() }
    suspend fun fetchCourses(): List<MasterItem>? = fetch { api.getCourse() }

    private suspend fun fetch(call: suspend () -> retrofit2.Response<MasterResponse>): List<MasterItem>? {
        return withContext(Dispatchers.IO) {
            try {
                val resp = call()
                if (resp.isSuccessful) resp.body()?.data else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    suspend fun getAlumniList(): List<Alumni>? = withContext(Dispatchers.IO) {
        val response = api.getAllAlumni()
        if (response.isSuccessful) response.body() else null
    }

    suspend fun addAlumni(request: AlumniRequest): Alumni? = withContext(Dispatchers.IO) {
        val response = api.addAlumni(request)
        if (response.isSuccessful) response.body() else null
    }



}
