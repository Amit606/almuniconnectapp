package com.kwh.almuniconnect.evetns

data class EventsResponse(
    val success: Boolean,
    val data: EventsData,
    val message: String,
    val correlationId: String?,
    val errors: Any?
)


data class EventsData(
    val items: List<EventDto>,
    val totalCount: Int,
    val pageNumber: Int,
    val pageSize: Int
)

data class EventDto(
    val eventId: String,
    val title: String,
    val description: String,
    val startAt: String,
    val endAt: String,
    val location: String,
    val isActive: Boolean,
    val createdAtUtc: String
)
// ✅ SINGLE DATA MODEL
data class Event(
    val title: String,
    val location: String,
    val description: String,
    val date: String,
    val price: String,
    val image: Int,
    val imageUrl: String? = null,
    val startAt: String,
    val endAt: String,
)