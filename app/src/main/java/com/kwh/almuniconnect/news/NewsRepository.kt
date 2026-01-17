package com.kwh.almuniconnect.news

import com.kwh.almuniconnect.api.ApiService

class NewsRepository(private val api: ApiService) {

    suspend fun getNews(
        pageNumber: Int,
        pageSize: Int
    ): Result<NewsResponse> {
        return try {
            val resp = api.getNews(pageNumber, pageSize)
            if (resp.isSuccessful) {
                val body = resp.body()
                Result.success(
                    body?.copy(items = body.items ?: emptyList())
                        ?: NewsResponse(emptyList(), 0, 0, 0)
                )
            } else {
                Result.failure(
                    Exception("HTTP ${resp.code()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
