package com.kwh.almuniconnect.news

import android.util.Log
import com.kwh.almuniconnect.api.ApiService

class NewsRepository(private val apiService: ApiService) {

    suspend fun fetchNews(
        page: Int,
        size: Int
    ): Result<Pair<List<NewsItem>, Int>> {
        return try {
            val response = apiService.getNews(page, size)

            if (response.isSuccessful) {
                val data = response.body()?.data
                Result.success(
                    Pair(
                        data?.items ?: emptyList(),
                        data?.totalCount ?: 0
                    )
                )
            } else {
                Result.failure(Exception("API Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
