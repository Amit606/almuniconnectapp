package com.kwh.almuniconnect.news

data class NewsResponse(
    val items: List<NewsItem>?,
    val totalCount: Int,
    val pageNumber: Int,
    val pageSize: Int
)

data class NewsItem(
    val newsId: String,
    val title: String,
    val content: String,
    val publishedAt: String,
    val imageUrl: String,
    val isActive: Boolean,
    val createdAtUtc: String
)
