package com.kwh.almuniconnect.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BranchRepository {
    private val api = NetworkClient.createService(ApiService::class.java)

    suspend fun fetchBranches(): List<MasterItem> = withContext(Dispatchers.IO) {
        try {
            val response = api.getBranches()
            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
