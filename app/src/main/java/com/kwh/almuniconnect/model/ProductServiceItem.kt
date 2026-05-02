package com.kwh.almuniconnect.model

data class ProductServiceItem(
    val productId: String,
    val type: String,
    val title: String,
    val description: String,
    val link: String,
    val alumniId: String,
    val alumniName: String,
    val courseName: String,
    val batch: String,
    val photoUrl: String?,
    val isActive: Boolean,
    val createdAtUtc: String,
    val srNo: Int
)