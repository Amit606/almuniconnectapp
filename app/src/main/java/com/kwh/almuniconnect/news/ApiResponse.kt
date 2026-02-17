package com.kwh.almuniconnect.news

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: T?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("correlationId")
    val correlationId: String?,

    @SerializedName("errors")
    val errors: Any?
)
data class NewsResponse(

    @SerializedName("items")
    val items: List<NewsItem> = emptyList(),

    @SerializedName("totalCount")
    val totalCount: Int,

    @SerializedName("pageNumber")
    val pageNumber: Int,

    @SerializedName("pageSize")
    val pageSize: Int
)
data class NewsItem(

    @SerializedName("newsId")
    val newsId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("publishedAt")
    val publishedAt: String,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("isActive")
    val isActive: Boolean,

    @SerializedName("createdAtUtc")
    val createdAtUtc: String
)
