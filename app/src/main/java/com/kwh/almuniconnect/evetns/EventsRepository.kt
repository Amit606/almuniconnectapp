package com.kwh.almuniconnect.evetns

import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.api.ApiService

class EventsRepository(
    private val api: ApiService
) {
    suspend fun fetchEvents(
        pageNumber: Int = 1,
        pageSize: Int = 10
    ): List<Event> {

        val response = api.getEvents(pageNumber, pageSize)

        return response.data.items.map { it.toUiEvent() }
    }
}
fun EventDto.toUiEvent(): Event {
    return Event(
        title = title,
        location = location,
        date = startAt.substring(0, 10), // simple formatting
        price = "Free",
        image = R.drawable.first, // local drawable
        startAt = startAt,
        endAt = endAt
    )
}

